/**
 * 
 */
package com.example.android.rhythwalk;

/**
 * @author kiefer7126
 *
 */
public class SongSituationDB {

	private int spring;
	private int summer;
	private int autumn;
	private int winter;

	SongSituationDB(int durationID) {

		switch (durationID) {
		case 238480:// 真っ赤な空を見ただろうか
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;

		case 276645:// 睡眠時間
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;

		case 323265:// 粉雪
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 15;
			break;

		case 217321:// 紫陽花の詩
			spring = 0;
			summer = 3;
			autumn = 0;
			winter = 0;
			break;
			
		case 366879:// さよならメモリーズ
			spring = 12;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 244186:// ホタルノヒカリ
			spring = 0;
			summer = 8;
			autumn = 0;
			winter = 0;
			break;
			
		case 295869:// 桃ノ花ビラ
			spring = 4;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 293042:// 夏の大三角形
			spring = 0;
			summer = 12;
			autumn = 0;
			winter = 0;
			break;
			
		case 271966:// 春風
			spring = 7;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 280866:// 花火
			spring = 0;
			summer = 15;
			autumn = 0;
			winter = 0;
			break;
			
		case 263679:// 3月9日
			spring = 6;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 317881:// 夏空
			spring = 0;
			summer = 16;
			autumn = 0;
			winter = 0;
			break;
			
		case 296229:// 秋桜
			spring = 0;
			summer = 0;
			autumn = 15;
			winter = 0;
			break;
			
		case 268460:// 蛍火
			spring = 0;
			summer = 11;
			autumn = 0;
			winter = 0;
			break;
			
		case 360075:// うたかた花火
			spring = 0;
			summer = 16;
			autumn = 0;
			winter = 0;
			break;
			
		case 281974:// 12月のリング
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 15;
			break;
			
		case 263166:// 君がいた夏
			spring = 0;
			summer = 11;
			autumn = 0;
			winter = 0;
			break;
			
		case 268464:// HANABI
			spring = 0;
			summer = 17;
			autumn = 0;
			winter = 0;
			break;
			
		case 411760:// メリークリスマス
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 14;
			break;
			
		case 358232:// SAKURA
			spring = 13;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 276000:// ミュージック・アワー
			spring = 0;
			summer = 9;
			autumn = 0;
			winter = 0;
			break;
			
		case 261546:// 卒業
			spring = 9;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 244619://　卒業写真
			spring = 6;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 222272:// 夏祭り
			spring = 0;
			summer = 17;
			autumn = 0;
			winter = 0;
			break;
			
		case 258043:// 夏空グラフィティ
			spring = 0;
			summer = 8;
			autumn = 0;
			winter = 0;
			break;
			
		case 253866:// 夏色惑星
			spring = 0;
			summer = 5;
			autumn = 0;
			winter = 0;
			break;
			
		case 250565:// 春景色
			spring = 4;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 260823:// 桜
			spring = 6;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 231027:// 桜並木道
			spring = 7;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
			
		case 369652:// スノースマイル
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 11;
			break;
		
		default:
			spring = 0;
			summer = 0;
			autumn = 0;
			winter = 0;
			break;
		}
	}

	public int getSpring() {
		return spring;
	}

	public int getSummer() {
		return summer;
	}

	public int getAutumn() {
		return autumn;
	}

	public int getWinter() {
		return winter;
	}

	public void setSpring(int spring) {
		this.spring = spring;
	}

	public void setSummer(int summer) {
		this.summer = summer;
	}

	public void setAutumn(int autumn) {
		this.autumn = autumn;
	}

	public void setWinter(int winter) {
		this.winter = winter;
	}
}
