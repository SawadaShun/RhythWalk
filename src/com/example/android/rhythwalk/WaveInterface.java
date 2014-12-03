package com.example.android.rhythwalk;

/**
 * 
 * 波形見るのにどんなインターフェースがほしいか
 * WavePlayerとWaveVisualizerで統一したいので用意した
 * 
 * @author mikankari
 *
 */
public interface WaveInterface {
	public byte[] getWaveform();
	public byte[] getFFT();
	public int getBPM();
}
