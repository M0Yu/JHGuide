//package jhunplay.com.block.tool;
//
//import java.util.ArrayList;
//
//import jhunplay.com.block.jhguide.ViewSpots;
//import android.content.Context;
//import android.content.pm.FeatureInfo;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.renderscript.Int3;
//
//public class DBTools {
//	Context context ;
//	
//	public DBTools(Context context) {
//		super();
//		this.context = context;
//	    // 初始化，只需要调用一次  
//	    AssetsDatabaseManager.initManager(context);   
//	}
//
//	public ArrayList<ViewSpots> getSpotByType(int num){
//		ArrayList<ViewSpots> list = new ArrayList<ViewSpots>();
//		AssetsDatabaseManager dbManager = AssetsDatabaseManager.getManager(); 
//		SQLiteDatabase database = dbManager.getDatabase("eastlake.db");
//		Cursor cursor = database.rawQuery("SELECT HotSpotName,HotSpotX,HotSpotY,HotInfo,FirstImageUri,HotSpotType FROM HOTSPOT ", null); 
//		Integer n = cursor.getCount();
//	    String[] data = new String[n];
//	    cursor.moveToFirst();
//	    int k = 0;
//	    while (!cursor.isAfterLast()) {
//	        	
//	        	ViewSpots spots =new ViewSpots().setName(cursor.getString(0)).setLocal_X(cursor.getDouble(1)).setLocal_Y(cursor.getDouble(2)).setInfo(cursor.getString(3)).setImgUrl(cursor.getString(4)).setType(cursor.getInt(5));
////	        String ss = cursor.getString(0) 
////	        		+ ", " + cursor.getString(1)
////	        		 + ", " + cursor.getDouble(2)
////	        		  + ", " + cursor.getDouble(3)
////	        		   + ", " + cursor.getString(4)
////	        		    + ", " + cursor.getString(5)
////	        		     + ", " + cursor.getString(6)
////	        		      + ", " + cursor.getInt(7)
////	        		       + ", " + cursor.getInt(8);
////	        data[k++] = ss;
//	       
//			if (spots.type==num) {
//					list.add(spots);
//			}
////	        
//	        cursor.moveToNext();
//	    }
//		return list;
//	}
//}
