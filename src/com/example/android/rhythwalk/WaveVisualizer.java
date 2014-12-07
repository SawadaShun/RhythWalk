package com.example.android.rhythwalk;

import java.util.HashMap;
import java.util.TreeSet;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;

/**
 * 
 * 波形見るクラス。別途MediaPlayerが必要
 * WavePlayerが重いため、代替案として用意
 * 
 * @author mikankari
 *
 */

// Android 2.3 (Gingerbread) 以上が必要
// 一時的にTargetApiを設定している
// 導入にはAndroidManifestいじる必要がある
@TargetApi(Build.VERSION_CODES.GINGERBREAD)

public class WaveVisualizer implements WaveInterface{

	private Visualizer visualizer;
	private WaveView waveview;
	private byte[] waveform;
	private byte[] waveform1000ms;
	private int waveform1000ms_index;
	private HashMap<Integer, Integer> bpms;
	private byte[] fft;
	private boolean isanalyze = false;

	/**
	 * 
	 * インスタンス生成
	 * 
	 * @param player 解析対象を再生するMediaPlayer
	 */
	public WaveVisualizer(MediaPlayer player){
    	visualizer = new Visualizer(player.getAudioSessionId());
    	visualizer.setEnabled(false);
    	visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    	visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate) {
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自動生成されたメソッド・スタブ
						// 波形の形にしている
//						for (int i = 0; i < waveform.length; i++) {
//							waveform[i] += 128;
//						}
						updateWaveform(waveform);

						// ウェーブレット解析結果生成
						if(isanalyze && waveform1000ms_index >= waveform1000ms.length){
							// BPM解析
							updateBPM();
						}
						
					}
				}).start();
			}
			
			@Override
			public void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
				// fft[0]:			直流成分の値（――、すなわち0Hz）
				// fft[1]:			サンプリング周波数の半分の実部
				// fft[2 * i]:		交流成分の実部（sinみたいな～～）
				// fft[2 * i + 1]:	交流成分の虚部（cosみたいな～～）
				// ここでは実部と虚部を計算済みの値にしている
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自動生成されたメソッド・スタブ
			    		for (int i = 1; i < fft.length / 2; i++) {
			    			double amplitude = Math.sqrt(Math.pow(fft[i * 2], 2) + Math.pow(fft[i * 2 + 1], 2));
			    			if(amplitude > Byte.MAX_VALUE){
			    				amplitude = Byte.MAX_VALUE;
			    			}
			    			fft[i * 2] = (byte)amplitude;
			    			fft[i * 2 + 1] = (byte)amplitude;
			    		}
			    		updateFFT(fft);
					}
				}).start();
			}
		},
		Visualizer.getMaxCaptureRate() / 2,
		true, isanalyze);	// waveform, fft
    	visualizer.setEnabled(true);
    	waveview = null;
    	
    	waveform = null;
    	waveform1000ms = null;
    	waveform1000ms_index = -1;
    	bpms = new HashMap<Integer, Integer>();
    	fft = null;
    }

    private void updateWaveform(byte[] waveform_origin){
    	waveform = new byte[waveform_origin.length];
    	for (int i = 0; i < waveform.length; i++) {
			waveform[i] = (byte)(waveform_origin[i] + 128);
		}
    	if(waveform1000ms_index >= 0 && waveform1000ms_index < waveform1000ms.length){
        	for (int i = 0; i < waveform.length && waveform1000ms_index + i < waveform1000ms.length; i++) {
    			waveform1000ms[waveform1000ms_index + i] = waveform[i];
    		}
        	waveform1000ms_index += waveform.length;
    	}else{
    		waveform1000ms = new byte[visualizer.getSamplingRate() / 1000];
    		waveform1000ms_index = 0;
    	}
    	if(waveview != null){
    		waveview.updateWaveform(waveform);
    	}
    }
    
    private void updateBPM(){
    	// BPM解析
    }
    
    private void updateFFT(byte[] fft){
       	this.fft = fft;    		
       	// FFT解析
    }

    /**
     * 
     * 波形表示に使用するWaveViewを返す
     * 
     * @return 
     */
    public WaveView getWaveView(){
    	return waveview;
    }
    
    /**
     * 
     * 波形表示に使用するWaveViewを設定する
     * 
     * @param view
     */
    public void setWaveView(WaveView view){
    	waveview = view;
    }
        
    /**
     * 再生中の波形を返す
     */
	@Override
	public byte[] getWaveform() {
		// TODO 自動生成されたメソッド・スタブ
		return waveform;
	}

	/**
	 * 再生中のFFTを返す
	 */
	@Override
	public byte[] getFFT() {
		// TODO 自動生成されたメソッド・スタブ
		return fft;
	}

	/**
	 * 将来用。再生中までのBPMを返す
	 */
	@Override
	public int getBPM() {
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
	 * 解析の開始
	 */
	public void start(){
		visualizer.setEnabled(true);
	}
	
	/**
	 * 解析の停止。再開はできない
	 */
	public void stop(){
		visualizer.setEnabled(false);
		visualizer.release();
	}
    
}


