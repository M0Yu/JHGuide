//package jhunplay.com.block.tool;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.DisplayMetrics;
//
//public class MyApplication {
//
//	public static class User{
//		public String nickName ;
//		public String name ;
//		public String passWord ;
//		public User(String nickName, String name, String passWord) {
//			super();
//			this.nickName = nickName;
//			this.name = name;
//			this.passWord = passWord;
//		}
//		
//		public Boolean isEmpty(){
//			if (this.name.equals("*")) {
//				return true ;
//			}
//			return false ;
//		}
//		public Boolean isLegal(){
//			
//			return true ;
//		}
//	}
//	public static class CGSize{
//		public double width,height;
//
//		public CGSize(double width, double height) {
//			super();
//			this.width = width;
//			this.height = height;
//		}
//		
//	}
//	public static CGSize getWindowsize(Context context){
//		
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		double w = displayMetrics.widthPixels;
//		double h = displayMetrics.heightPixels;
//		CGSize size = new CGSize(w,h) ;
//		return size ;
//	}
//	
//	public static User getCurrentUser(Context context){
//		SharedPreferences preferences = context.getSharedPreferences("user", 0);
//		String nickName = preferences.getString("nickName", "default");
//		String name = preferences.getString("name", "*");
//		String passWord = preferences.getString("passWord", "*");
//		User user = new User(nickName, name, passWord) ;
//		return  user.isEmpty()?null:user;
//	}
//	
//	public static void changeCurrentUser(Context context , User user){
//		if (user.isLegal()) {
//			SharedPreferences preferences = context.getSharedPreferences("user", 0);
//			SharedPreferences.Editor editor = preferences.edit();
//			editor.clear() ;
//			editor.putString("nickName", user.nickName);
//			editor.putString("name", user.name);
//			editor.putString("passWord", user.passWord);
//			editor.commit();
//		}
//	}
//	public static void setCurrentUser(Context context , User user){
//		changeCurrentUser(context, user);
//	}
//}
