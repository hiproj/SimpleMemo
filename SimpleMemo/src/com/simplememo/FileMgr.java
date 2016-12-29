package com.simplememo;

/**
 * Created by USER on 2016-11-28.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileMgr {


	public void main2(){
//		DisplayPrint dpp = new DisplayPrint();
//		String filePath = this.getClass().getResource("/" + "TestFile.txt").toString();
//		dpp.append(filePath);

//		URL urlToDictionary = this.getClass().getResource("/" + "TestFile.txt");
//		dpp.append(urlToDictionary.toString());
//		InputStream stream = null;
//		try {
//			stream = urlToDictionary.openStream();
//			StringBuffer sb = new StringBuffer();
//			byte[] b = new byte[4096];
//			for (int n; (n = stream.read(b)) != -1;) {
//			    sb.append(new String(b, 0, n));
//			}
//			dpp.append(sb.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}



//		for(String str : loadFileTextArray2("testpkg/TestFile2.txt", "UTF-8")){
//			System.out.println(str);
//			dpp.append(str);
//		}
//

	}
	//
	public static String defPath = "";

	static String ENC_UTF8 = "UTF-8";
	static String ENC_UNICODE = "Unicode";
	static String ENC_EUC_KR = "EUC_KR";

	public static String[] loadFileTextArray(String filePath){
		return loadFileTextArray(filePath, ENC_UTF8);
	}

	/**
	 *
	 * @param filePath
	 * @param enc : UTF-8 / Unicode / EUC_KR(ANSI의 한글)
	 * @return
	 */
	public static String[] loadFileTextArray(String filePath, String enc){
		ArrayList<String> ar = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			//Reader reader = new InputStreamReader(fis, "euc-kr");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, enc));
			String line = null;
			while ((line = br.readLine()) != null) {
				ar.add(line);
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (ar.size() > 0)
			ar.set(0, removeUTF8BOM(ar.get(0)));
		System.out.println("파일 로드 완료 : " + filePath + " / enc : " + enc);
		return ar.toArray(new String[ar.size()]);
	}

	public static String[] loadFileTextArray2(String filePath, String enc){
		URL urlToPath = FileMgr.class.getClass().getResource("/" + filePath);
		ArrayList<String> ar = new ArrayList<String>();
		try {
			InputStream fis = urlToPath.openStream();
			//Reader reader = new InputStreamReader(fis, "euc-kr");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, enc));
			String line = null;
			while ((line = br.readLine()) != null) {
				ar.add(line);
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ar.size() > 0)
			ar.set(0, removeUTF8BOM(ar.get(0)));
		System.out.println("파일 로드 완료 : " + filePath + " / enc : " + enc);
		return ar.toArray(new String[ar.size()]);
	}

	public static final String UTF8_BOM = "\uFEFF";

	private static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}

	public static void saveFileText(String filePath, String[][] text, String sep, String enc, boolean isAppend){
		System.out.println("saveFileText_filePath : " + filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			File fileDir = new File(file.getParent());
			fileDir.mkdirs();
		}

		try {
//			if(!isAppend)
//				new FileOutputStream(filePath).close();

			FileOutputStream  fw = new FileOutputStream (filePath, isAppend); // 절대주소 경로 가능
			OutputStreamWriter ow = new OutputStreamWriter(fw, enc);
			BufferedWriter bw = new BufferedWriter(ow);
			if(text.length == 1){
				bw.write(text[0][0]);
				bw.newLine();
			} else
				for(int i = 0; i < text.length; i++){
					bw.write(text[i][0]);
					int len = text[i].length;
					for(int j = 1; j < len; j++){
						bw.write(sep + text[i][j]);
					}
					bw.newLine(); // 줄바꿈
				}
			System.out.println("파일 저장 완료 : " + filePath);
			bw.close();
			fw.close(); // 일단 해봄. 문제 없으면 그냥 쓰기.
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
	}

	public static void saveFileText(String filePath, String text, String enc, boolean isAppend){
		saveFileText(filePath, new String[][]{{text}}, "", enc, isAppend);
	}
	public static void saveFileText(String filePath, List<String> list, String enc, boolean isAppend){
		saveFileText(filePath, list.toArray(new String[list.size()]), enc, isAppend);
	}


	public static void saveFileText(String filePath, String[] text, String enc, boolean isAppend){
		String str2[][] = new String[text.length][1];
		for (int i = 0; i < text.length; i++) {
			str2[i][0] = text[i];
		}
		saveFileText(filePath, str2, "", enc, isAppend);
	}
}

