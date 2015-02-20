package com.example.android.rhythwalk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

/**
 * 
 * 波形見るクラス。プレイヤーにもなる
 * 
 * @author mikankari
 *
 */

// Android 4.1 (JellyBean) 以上が必要
// 一時的にTargetApiを設定している
// 導入にはAndroidManifestいじる必要がある
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)

public class WavePlayer {
	
	private MediaExtractor extractor;
	private MediaFormat format;
	private MediaCodec codec;
	private Thread buffer_thread;
	private boolean buffer_isloop;
	private byte[] waveform;
	private byte[] waveform1000ms;
	private int waveform1000ms_index;
	private byte[] wavelet_s1;
	private byte[] wavelet_w1;
	private HashMap<Integer, Integer> bpms;
	private boolean buffer_isplay = false;
	private byte[] fft;
	/**
	 * getBPMにおいて、解析不可のときの戻り値
	 */
	public static final int UNKNOWN_BPM = -1;

	/**
	 * 
	 * インスタンス生成
	 * 
	 * @param uri ファイルのURI
	 * @throws IOException 
	 */
	public WavePlayer(String uri) throws IOException{
		extractor = new MediaExtractor();
		extractor.setDataSource(uri);
		extractor.selectTrack(extractor.getTrackCount() - 1);
		format = extractor.getTrackFormat(extractor.getTrackCount() - 1);
		String mimetype = format.getString(MediaFormat.KEY_MIME);

		codec = MediaCodec.createDecoderByType(mimetype);
		codec.configure(format, null, null, 0);
		codec.start();
		
		final ByteBuffer[] inputbuffer = codec.getInputBuffers();
		final ByteBuffer[] outputbuffer = codec.getOutputBuffers();
				
		buffer_thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				boolean isEOS = false;
				MediaCodec.BufferInfo bufferinfo = new MediaCodec.BufferInfo();
				AudioTrack track = null;
				byte[] chunk = null;
				boolean isplayed = false;

				while(buffer_isloop){
					int inputbuffer_index = codec.dequeueInputBuffer(1000000);
					if(inputbuffer_index >= 0){
						ByteBuffer buffer = inputbuffer[inputbuffer_index];
						int bufferSize = extractor.readSampleData(buffer, 0);
						long presentationTimeUs = 0;
						if(bufferSize < 0){
							bufferSize = 0;
							isEOS = true;
							Log.d("", "End of Stream");
							break;
						}else{
							presentationTimeUs = extractor.getSampleTime();
						}
						codec.queueInputBuffer(inputbuffer_index,
												0,
												bufferSize, 
												presentationTimeUs,
												!isEOS ? 0 : MediaCodec.BUFFER_FLAG_END_OF_STREAM);
						if(!isEOS){
							extractor.advance();
						}
					}else{
//						Log.d("", "fail to get inputbuffer");
						try {
							Thread.sleep(16);
						} catch (Exception e) {
							Log.e("", e.toString() + " in buffer");
						}
					}
				
					int response = codec.dequeueOutputBuffer(bufferinfo, 1000);
					if(response >= 0){
						int outputbuffer_index = response;
						ByteBuffer buffer = outputbuffer[outputbuffer_index];
						if(chunk == null || chunk.length < bufferinfo.size){
							chunk = new byte[bufferinfo.size];
						}
						buffer.position(bufferinfo.offset);
						buffer.get(chunk, 0, chunk.length);
						if(buffer_isplay && bufferinfo.size > 0){
							int remaining = chunk.length;
							int written = 0;
							int written_once;
							while(true){
								written_once = track.write(chunk, written, remaining);
								written += written_once;
								remaining -= written_once;
								if(!isplayed && (remaining == 0 || written_once == 0)){
									isplayed = true;
									track.play();
								}
								if(remaining == 0){
									break;
								}
								try {
									Thread.sleep(16);
								} catch (Exception e) {
									Log.e("", e.toString() + " in outputbuffer");
								}
							}
						}
						updateWaveform(chunk);
						codec.releaseOutputBuffer(outputbuffer_index, false);
						if((bufferinfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0){
							break;
						}
						buffer.clear();
						
					}else if(response == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED){
//						outputbuffer = codec.getOutputBuffers();
						
					}else if(response == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
						int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
						int[] channelConfigs = {AudioFormat.CHANNEL_OUT_MONO, AudioFormat.CHANNEL_OUT_STEREO};
						int channelConfig = channelConfigs[format.getInteger(MediaFormat.KEY_CHANNEL_COUNT) - 1];
						int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
						int bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);

						track = new AudioTrack(AudioManager.STREAM_MUSIC,
															sampleRate,
															channelConfig,
															audioFormat,
															bufferSize,
															AudioTrack.MODE_STREAM);
						Log.d("", sampleRate + "Hz " + format.getInteger(MediaFormat.KEY_CHANNEL_COUNT) + "ch buffer:" + bufferSize + "");
					}
				}
			}
			
		});
		
		waveform = null;
		waveform1000ms = null;
		waveform1000ms_index = -1;
		wavelet_s1 = null;
		wavelet_w1 = null;
		bpms = new HashMap<Integer, Integer>();
		fft = null;
	}
	
	private void updateWaveform(byte[] waveform_sample){
		waveform = waveform_sample;
		if(waveform != null){
			updateFFT();
		}
    	if(waveform1000ms_index >= 0 && waveform1000ms_index < waveform1000ms.length){ 
    		for (int i = 0; i < waveform_sample.length && waveform1000ms_index + i < waveform1000ms.length; i++) { 
    			waveform1000ms[waveform1000ms_index + i] = waveform_sample[i]; 
    		}
        	waveform1000ms_index += waveform_sample.length; 
    	}else{
    		if(waveform1000ms != null){
        		updateWavelet();
    		}
    		waveform1000ms = new byte[format.getInteger(format.KEY_SAMPLE_RATE)]; 
    		waveform1000ms_index = 0; 
    	}
	}
	
	private void updateWavelet(){
		// ウェーブレット変換
		
		wavelet_w1 = new byte[waveform1000ms.length / 2];
		wavelet_s1 = new byte[waveform1000ms.length / 2];
		for (int j = 0; j < waveform1000ms.length / 2 - 1; j++) {
			int average = (waveform1000ms[j * 2] + waveform1000ms[j * 2 + 1]) / 2;
			wavelet_s1[j] = (byte)average;
			int difference = (waveform1000ms[j * 2] - waveform1000ms[j * 2 + 1]);
			wavelet_w1[j] = (byte)difference;
		}
		updateBPM();
	}
	
	private void updateBPM(){
		// テンポ解析		
		
		// 波形の差分をdevision個に分割して最大値、最小値を見る
		int division = 10;
		int range = wavelet_w1.length / division;
		int[] min_indexes = new int[division];
		byte[] min_values = new byte[division];
		int[] max_indexes = new int[division];
		byte[] max_values = new byte[division];
		byte values_min = Byte.MAX_VALUE;
		byte values_max = Byte.MIN_VALUE;
		for (int i = 0; i < wavelet_w1.length - range; i += range) {
			byte min = Byte.MAX_VALUE;
			int min_index = 0;
			byte max = Byte.MIN_VALUE;
			int max_index = 0;
			for (int j = 0; j < range; j++) {
				int index = i + j;
				byte value = wavelet_w1[index];
				if(min > value){
					min = value;
					min_index = index;
				}
				if(max < value){
					max = value;
					max_index = index;
				}
			}
			int index = i / range;
			min_indexes[index] = min_index;
			min_values[index] = min;
			max_indexes[index] = max_index;
			max_values[index] = max;
			if(values_min > min){
				values_min = min;
			}
			if(values_max < max){
				values_max = max;
			}
		}
		// 得られた最大値、最小値のうちしきい値を超えた有益なものを抽出
		double threshold = 0.4;
		double min_threshold = values_min * threshold;
		double max_threshold = values_max * threshold;
		int minimum_duration = wavelet_w1.length / division;
		ArrayList<Integer> beatmin_indexes = new ArrayList<Integer>();
		ArrayList<Integer> beatmax_indexes = new ArrayList<Integer>();
		for (int i = 0; i < min_indexes.length; i++) {
			if(min_values[i] < min_threshold){
//				if(beatmin_indexes.isEmpty()){	//	精度向上の余地ありかも
//					beatmin_indexes.add(min_indexes[i]);								
//				}else if(min_indexes[i] - beatmin_indexes.get(beatmin_indexes.size() - 1) >= minimum_duration){
//					beatmin_indexes.add(min_indexes[i]);
//				}
				
//				if(次のビートとの間隔が長い)	そのまま追加
//				else	平均して追加
			}
			if(max_values[i] > max_threshold){
				if(beatmax_indexes.isEmpty() || max_indexes[i] - beatmax_indexes.get(beatmax_indexes.size() - 1) >= minimum_duration){
					beatmax_indexes.add(max_indexes[i]);
				}
			}
		}
		// 有益な最大値、最小値の間隔を見てBPMを求める
		if(beatmin_indexes.size() == 0){
			beatmin_indexes.add(0);
		}
		if(beatmax_indexes.size() == 0){
			beatmax_indexes.add(0);
		}
		int[] bpmmin = new int[beatmin_indexes.size() - 1];
		int[] bpmmax = new int[beatmax_indexes.size() - 1];
		int maximum_bpm = 185;
		double msper1sample = 1085.9 / (format.getInteger(format.KEY_SAMPLE_RATE) / 2);	// 1085.9 は環境依存要素かもしれない。1000msを表している
		for (int i = 0; i < beatmin_indexes.size() - 1; i++) {
			int duration_sample = beatmin_indexes.get(i + 1) - beatmin_indexes.get(i);
			bpmmin[i] = (int)(60000 / (duration_sample * msper1sample));
			while(bpmmin[i] > maximum_bpm){
				bpmmin[i] /= 2;
			}
		}
		for (int i = 0; i < beatmax_indexes.size() - 1; i++) {
			int duration_sample = beatmax_indexes.get(i + 1) - beatmax_indexes.get(i);
			bpmmax[i] = (int)(60000 / (duration_sample * msper1sample));						
			while(bpmmax[i] > maximum_bpm){
				bpmmax[i] /= 2;
			}
		}
		// 求まったBPM候補をグローバル変数へ送る
		for (int i = 0; i < bpmmin.length; i++) {
			int key = bpmmin[i];
			if(bpms.containsKey(key)){
				int value = bpms.get(key);
				bpms.put(key, value + 1);				
			}else{
				bpms.put(key, 0);
			}
		}
		for (int i = 0; i < bpmmax.length; i++) {
			int key = bpmmax[i];
			if(bpms.containsKey(key)){
				int value = bpms.get(key);
				bpms.put(key, value + 1);				
			}else{
				bpms.put(key, 0);
			}
		}
	}
	
	private void updateFFT(){
		// 高速フーリエ変換
		// ここでは行わず、WaveVisualizerで行う
		updateScale();
	}
	
	private void updateScale(){
		// スケール解析
		// ここでは行わず、WaveVisualizerで行う		
	}
		
	/**
	 * 
	 * 解析中、または再生中の波形を返す
	 * 
	 * @return
	 */
	public byte[] getWaveform(){
		byte[] waveform_sample = waveform;
		return waveform_sample;
	}
		
	/**
	 * 
	 * 解析中、または再生中までの解析したテンポをBPMで返す<br>
	 * 
	 * @return
	 */
	public int getBPM(){
		int max = Integer.MIN_VALUE;
		int max_index = UNKNOWN_BPM;
		Integer[] keys = (Integer[]) new TreeSet(bpms.keySet()).toArray(new Integer[0]);
		for (int i = 0; i < keys.length; i++) {
			Integer count = bpms.get(keys[i]);
			if(max < count){
				max = count;
				max_index = keys[i];
			}
		}
		return max_index;
	}
		
	/**
	 * 
	 * 解析中、または再生中の波形の高速フーリエ変換した結果を返す<br>
	 * スケール解析はWaveVisualizerで行うため、ここでは常にnullを返す<br>
	 * 
	 * @return
	 */
	public byte[] getFFT(){
		return fft;
	}
	
	/**
	 * 
	 * 将来用。解析中、または再生中までの解析したスケールを返す
	 * 
	 * @return 
	 */
	public String getScale(){
		// 遠藤による提案：これで外部からアクセスできると楽しい
		return "";
	}

	/**
	 * 
	 * 解析中or再生中の位置をマイクロ秒で返す
	 * 
	 * @return
	 */
	public double getCurrentTime(){
		double current = extractor.getSampleTime();
		if(current < 0){
			current = getDurationTime();
		}
		return current;
	}
	
	/**
	 * 
	 * 解析中or再生中の長さをマイクロ秒で返す
	 * 
	 * @return
	 */
	public double getDurationTime(){
		double duration = format.getLong(MediaFormat.KEY_DURATION);
		return duration;
	}
	
	/**
	 * 解析の開始
	 */
	public void start(){
		buffer_isloop = true;
		buffer_thread.start();
	}
	
	/**
	 * 解析の停止。再開はできない
	 */
	public void stop(){
		buffer_isloop = false;
		try {
			buffer_thread.join();
		} catch (Exception e) {
			Log.e("", e.toString() + " in stop");
		}
		codec.stop();
		codec.release();
	}

}
