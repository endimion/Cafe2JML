package model;

public class StringHelper {

	
	
	/**
	 * Helper method which cuts a string when it encounters a whitespace
	 * @param s, the string we want to cut at the first whitespace 
	 * @return the substring of s which is cut at the first encounter of a whitespace 
	 * if it exists else s is returned
	*/
	public static String cutStringAtWhite(String s){
		int index = 0;
		for(int i=0; i< s.length(); i++ ){
			if(s.toCharArray()[i] == ' '){ index = i; break;}
		};
		return (index !=0)? s.substring(0,index): s;
	}//end of cutStringAtWhite
	
	
	
	/**
	 * 
	 * @param s, a String
	 * @return an integer indicating the first appearence of a whitespace
	 */
	public static int getWhitePos(String s){
		int index = 0;
		for(int i=0; i< s.length(); i++ ){
			if(s.toCharArray()[i] == ' '){ index = i; break;}
		};
		return index;
	}//end of cutStringAtWhite
	
	
	
	
	
	/**
	 * Helper method which takes a String and removes the last character of that string
	 * @param the_string
	 * @return
	 */
	public static String remLastChar(String the_string){
		return the_string.substring(0, the_string.length()-1);
	}
	
	/**
	 * removes the first character of a given String
	 * @param the_string
	 * @return the input string without the first character
	 */
	public static String remFirstChar(String the_string){
		return the_string.substring(1, the_string.length());
	}
	
	

	/** Returns the number of times a character appears into a given string
	 * @param word, a string we want to search into
	 * @param c , a character we are searching the occurrences of
	 */
	public static int numOf(String word, char c){
		char[] wordArr = word.toCharArray();
		int num = 0;
		for(char ch : wordArr){
			if(ch == c){num++;}
		}
		return num;
	}//end of numOf
	
	
	/**
	 * 
	 * @param st
	 * @return the given string without any whitespaces
	 */
	public static String remWhite(String st){
		
		String res ="";
		char[] orig = st.toCharArray();
		
		for(char ch : orig){
			if(ch != ' '){res = res + ch;}
		}
		
		return res;
	}//returns the i
	
	
	/**
	 * Removes from a given string special characters like parenthesis and brackets
	 * @param s
	 * @return a string without "(" or ")" or "[" and "]"
	 */
	public static String removeSpecialCharacters(String s){
		
		return (s.replace("[", "")).replace("]","").replace("(","").replace(")", "").replace(",","");
		
	}
	
	
	/**
	 * takes as input a string and returns an integer indicating the
	 * position where the first to open parenthisis closes in that string
	 * i.e. in the string: asd(dd)23 the result will be 6 
	 * @return
	 */
	public static int colsingParPosition(String s){
		int leftParenCount = 0;
		int rightParenCount = 0;
		int pos = -1;
		char c;
		
		for(int i = 0; i < s.toCharArray().length;i++){
			c = s.toCharArray()[i];
			
			if(c == '('){
				leftParenCount++;
			}
			if(c == ')'){
				rightParenCount++;
			}
			if(leftParenCount == rightParenCount && leftParenCount != 0){ 
				pos =  i;
				break;
			}
		}//end of looping through the characters of the line
		
		return pos;
	}//end of closingParPosition
	
	
	/**
	 * example: asd(dd)23 the result will be 3
	 * @param s a String
	 * @param search a character we want to search the string for
	 * @return the position where the search character appears for the first time in the given string
	 */
	public static int firstAppearOfChar(String s, char search){
		int firstOpenPos = -1;
		
		char c;
		
		for(int i = 0; i < s.toCharArray().length;i++){
			c = s.toCharArray()[i];
			if(c == search){
				firstOpenPos  = i;
				break;
			}
		}//end of looping through the characters of the line
		return firstOpenPos;
	}//end of firstAppearOfChar 
	
	
	
	
	
	
}//end of class
