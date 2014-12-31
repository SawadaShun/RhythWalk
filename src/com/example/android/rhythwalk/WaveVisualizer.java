package com.example.android.rhythwalk;

import java.util.HashMap;
import java.util.TreeSet;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.util.Log;

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

public class WaveVisualizer{

	private Visualizer visualizer;
	private WaveView waveview;
	private byte[] waveform;
	private byte[] waveform1000ms;
	private int waveform1000ms_index;
	private byte[] wavelet_s1;
	private byte[] wavelet_w1;
	private HashMap<Integer, Integer> bpms;
	private boolean isanalyze = true;
	/**
	 * getBPMにおいて、解析不可のときの戻り値
	 */
	public static final int UNKNOWN_BPM = -1;
	//ここから赤木の追加分変数
	private byte[] fft; //fftデータ格納用バイト型変数
	private boolean Scale = false;//スケールが検出できているか、いないかの判定
	private int max=0; //最大値座標格納用変数
	private int cmj=0,cma=0,dmj=0,dma=0,emj=0,ema=0,fmj=0,fma=0,gmj=0,gma=0,amj=0,bmj=0,bma=0;//各スケール比率加点方式用変数,mjはメジャー,maはマイナー
	private boolean a=false,b=false,c=false,d=false,e=false,f=false,g=false;//各音階判定用変数
	private boolean as=false,cs=false,ds=false,fs=false,gs=false;//(追加分)音階がシャープの時用のフラグ、ド,レ,ファ,ソ,ラの5音のみシャープあり
	private boolean majar=false,mainare=false; //メジャー、マイナーのフラグ、メジャーなら明るい、マイナーなら暗い曲として認定、各スケールに使用される特徴があるか調べてみる。
	//ここまで赤木の追加分変数
	

	/**
	 * 
	 * インスタンス生成
	 * 
	 * @param player 解析対象を再生するMediaPlayer
	 */
	public WaveVisualizer(MediaPlayer player){
    	visualizer = new Visualizer(player.getAudioSessionId());
    	visualizer.setEnabled(false);
    	visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);	// 最大のキャプチャサイズ
    	visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate) {
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						updateWaveform(waveform);

						if(isanalyze && waveform1000ms_index >= waveform1000ms.length){
							updateWavelet();
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
		Visualizer.getMaxCaptureRate(),	// 最大のキャプチャレート
		true, isanalyze);	// waveform, fft
    	visualizer.setEnabled(true);
    	waveview = null;
    	
    	waveform = null;
    	waveform1000ms = null;
    	waveform1000ms_index = -1;
    	wavelet_s1 = null;
    	wavelet_w1 = null;
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
    
    private void updateWavelet(){
    	// ウェーブレット変換
    	// ここでは行わず、WavePlayerで行う
    	updateBPM();
    }
    
    private void updateBPM(){
    	// テンポ解析
    	// ここでは行わず、WavePlayerで行う
    }
    
    private void updateFFT(byte[] fft){
       	// 高速フーリエ変換
       	// visualizerが勝手にやってくれる
       	this.fft = fft;    		

       	updateScale();
    }
    
	//赤木追加分,ｆｆｔの処理をここにガリガリと書いていきます	
    private void updateScale(){
       	// スケール解析

    	//最大値検出処理
		if(Scale==false){
			byte max_value;
			byte tmp=0;
			for(int loop=2;loop<51;loop+=2)
			{
				
					if(tmp<fft[loop] /*&& (_clonefft[loop2]!=0 && _clonefft[loop2-2]!=0)*/)
					{
						/*byte tmp = _clonefft[loop2];
						_clonefft[loop2]=_clonefft[loop2-2];
						_clonefft[loop2-2]=tmp;*/
						tmp = fft[loop];
						max=loop;
						max_value = tmp;
					}
				//}
			}
			/*ミの判定、ミの周波数はこの番号に格納されているはず。*/
			/*maxが14「ミ」かつ「ミ」が127の強さを持っていた場合、また前後の周波数成分が50以下の場合、「ミ」と判断する*/
			while(max > 23){	// 倍音補正かけてみたり（雑 追記(赤木):三倍音などにも対応できるようにしてみました
				max = max / (max/23+1) ;
			}
			// 書き込みテストです、テスト
							// fft[max] > max_value * 0.5 で最大値50%みたいにしてみたり
			
			if(max==12 && ((double)fft[max-2]/(double)fft[max])<0.5 && ((double)fft[max+2]/(double)fft[max])<=0.3 && ((double)fft[max+4]/(double)fft[max])<0.2 && cs==false)//ド#の検出
			{
				Log.d("音階","ド#");cs=true;dmj++;emj++;fma++;amj++;bmj++;bma++;
			}
			if(max==12 && ((double)fft[max-2]/(double)fft[max])<0.5 && ((double)fft[max+2]/(double)fft[max])>=0.5 && ((double)fft[max+2]/(double)fft[max])<=0.75 && ((double)fft[max+4]/(double)fft[max])>=0.2 && d==false)//レの検出
			{
				Log.d("音階","レ");d=true;cmj++;cma++;dmj++;ema++;fmj++;gmj++;gma++;bma++;
			}
			if(max==12 && ((double)fft[max-2]/(double)fft[max])>0.39 && ((double)fft[max+2]/(double)fft[max])>=0.9 && ((double)fft[max+4]/(double)fft[max])>0.35 && ds==false )//レ#の検出
			{
				Log.d("音階","レ#");ds=true;cma++;emj++;fma++;gma++;bmj++;
			}
			if(max==14 && ((double)fft[max-2]/(double)fft[max])<=0.15 && e==false)//ミの検出
			{
				Log.d("音階","ミ");e=true;cmj++;dmj++;dma++;emj++;ema++;fmj++;gmj++;amj++;bmj++;bma++;
			}
			if(max==16 && ((double)fft[max-2]/(double)fft[max])>=0.7 && ((double)fft[max+2]/(double)fft[max]) >= 0.40 && f==false)//ファの検出
			{
				Log.d("音階","ファ");f=true;cmj++;cma++;dma++;fmj++;fma++;gma++;
			}
			if(max==16 && ((double)fft[max-2]/(double)fft[max])<=0.5 && ((double)fft[max+2]/(double)fft[max])<=0.5 && fs==false)//ファ#の検出
			{
				Log.d("音階","ファ#");fs=true;dmj++;emj++;ema++;gmj++;amj++;bmj++;bma++;
			}
			if(max==18 && ((double)fft[max-2]/(double)fft[max])>=0.50 && ((double)fft[max+2]/(double)fft[max]) <= 0.50 && g==false)//ソの検出
			{
				Log.d("音階","ソ");g=true;cmj++;cma++;dmj++;dma++;ema++;fmj++;fma++;gmj++;gma++;bma++;
			}
			if(max==18 && ((double)fft[max+2]/(double)fft[max])<=0.30 && ((double)fft[max+2]/(double)fft[max])<0.5 && gs==false)//ソ#の検出
			{
				Log.d("音階","ソ#");gs=true;cma++;emj++;fma++;amj++;bmj++;
			}
			if(max==20 && ((double)fft[max-2]/(double)fft[max])>=0.5 && ((double)fft[max+2]/(double)fft[max])<0.5 && a==false)//ラの検出
			{
				Log.d("音階","ラ");a=true;cmj++;dmj++;dma++;emj++;ema++;fmj++;gmj++;gma++;amj++;bma++;
			}
			if(max==20 && ((double)fft[max-2]/(double)fft[max])<0.5 && ((double)fft[max+2]/(double)fft[max])<0.5 && as==false )//ラ#の検出
			{
				Log.d("音階","ラ#");as=true;cma++;dma++;emj++;ema++;fmj++;gma++;bmj++;
			}
			if(max==22 && ((double)fft[max-2]/(double)fft[max])>=0.5 && ((double)fft[max+2]/(double)fft[max])<=0.5 && b==false )//シの検出
			{
				Log.d("音階","シ");b=true;cmj++;dmj++;emj++;ema++;gmj++;amj++;bmj++;bma++;
			}
			if(max==22 && ((double)fft[max-2]/(double)fft[max])<=0.3 && c==false )//ドの検出
			{
				Log.d("音階","ド");c=true;cmj++;cma++;dma++;ema++;fmj++;fma++;gmj++;gma++;dmj--;
			}
			// Log.d("", (c ? "C" : "-") + (d ? "D" : "-") + (e ? "E" : "-") + (f ? "F" : "-") + (g ? "G" : "-") + (a ? "A" : "-") + (b ? "B" : "-") + );

			//各種スケールの検出を行う、取得された音階を参考にスケールの判定を行う
			if(Scale!=true){

				if(cmj==6)
				{
					Log.d("スケール","Cメジャースケール");
					majar = true;
					Scale=true;
				}
				if(cma==6)
				{
					Log.d("スケール","Cマイナースケール");
					mainare=true;
					Scale=true;
				}
				//ここまでCスケール
				//ここからDスケール
				if(dmj==6)
				{
					Log.d("スケール","Dメジャースケール");
					majar = true;
					Scale = true;
				}
				if(dma==6)
				{
					Log.d("スケール","Dマイナースケール");
					mainare = true;
					Scale = true;
				}
				//ここまでDスケール
				//ここからEスケール
				if(emj==6)
				{
					Log.d("スケール","Dメジャースケール");
					majar = true;
					Scale = true;
				}
				if(ema==6)
				{
					Log.d("スケール","Dマイナースケール");
					mainare = true;
					Scale = true;
				}
				//ここまでEスケール
				//ここからFスケール
				if(fmj==6)
				{
					Log.d("スケール","Dメジャースケール");
					majar = true;
					Scale = true;
				}
				if(fma==6)
				{
					Log.d("スケール","Dマイナースケール");
					mainare = true;
					Scale = true;
				}
				//ここからGスケール
				if(gmj==6)
				{
					Log.d("スケール","Dメジャースケール");
					majar = true;
					Scale = true;
				}
				if(gma==6)
				{
					Log.d("スケール","Dマイナースケール");
					mainare = true;
					Scale = true;
				}
				//ここからaスケール
				if(amj==6)
				{
					Log.d("スケール","Dメジャースケール");
					majar = true;
					Scale = true;
				}
				//ここからbスケール
				if(bmj==6)
				{
					Log.d("スケール","Dメジャースケール");
					majar = true;
					Scale = true;
				}
				if(bma==6)
				{
					Log.d("スケール","Dマイナースケール");
					mainare = true;
					Scale = true;
				}
			}
		}
	}
	//追加分、ここで終了

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
     * 
     * 再生中の波形を返す
     * 
     * @return 
     */
	public byte[] getWaveform() {
		byte[] waveform_sample = waveform;
		return waveform_sample;
	}

	/**
	 * 
	 * 再生中までの解析したテンポをBPMで返す<br>
	 * テンポ解析はWavePlayerで行うため、ここでは常にWaveVisualizer.UNKNOWN_BPMを返す<br>
	 * 
	 * @return 
	 */
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
	 * 
	 * 再生中の波形の高速フーリエ変換した結果を返す
	 * 
	 * @return
	 */
	public byte[] getFFT() {
		return fft;
	}

	/**
	 * 
	 * 将来用。再生中までの解析したスケールを返す
	 * 
	 * @return 
	 */
	public String getScale(){
		// 遠藤による提案：これで外部からアクセスできると楽しい
		return "";
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
