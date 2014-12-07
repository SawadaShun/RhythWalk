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
	int DEFAULT_BPM = -1;	// まだ解析していない楽曲に
	int UNKNOWN_BPM = 0;	// 解析できない楽曲に
	public byte[] getWaveform();
	public byte[] getFFT();
	public int getBPM();
}
