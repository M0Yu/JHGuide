package jhunplay.com.db;

import java.util.ArrayList;

import org.json.JSONObject;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作类
 * 
 * @author chenws
 * 
 */
public class DBService {

	/**
	 * 本地保存插入任务到数据库
	 * 
	 * @param context
	 * @param values
	 * @return
	 */
	public static long insert(Context context, ContentValues values) {
		DBHepler dbHepler = new DBHepler(context);
		SQLiteDatabase db = dbHepler.getWritableDatabase();
		String[] columns = new String[] { "id", "name", "other_name", "details", "pic", "xlong", "xlat", "style", "map_style" };
		Cursor c = db.query(DBHepler.T_WORK_TABLE, columns, "id=?", new String[] { (String) values.get("id") }, null, null, null);
		long id = 0;
		if (c.moveToFirst()) {
			id = db.update(DBHepler.T_WORK_TABLE, values, "id=?", new String[] { (String) values.get("id") });
		} else {
			id = db.insert(DBHepler.T_WORK_TABLE, "id", values);
		}
		db.close();
		dbHepler.close();
		return id;
	}

	/**
	 * 根据id删除任务
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static void detele(Context context) {
		DBHepler dbHepler = new DBHepler(context);
		SQLiteDatabase db = dbHepler.getWritableDatabase();
		String[] columns = new String[] { "id", "name", "other_name", "details", "pic", "xlong", "xlat", "style", "map_style" };
		Cursor c = db.query(DBHepler.T_WORK_LOG, columns, null, null, null, null, null);
		long idlong = 0;
		if (c.moveToFirst()) {
			idlong = db.delete(DBHepler.T_WORK_LOG, null, null);
		}
		db.close();
		dbHepler.close();
	}

	/**
	 * 查询本地任务
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<MapPoint> selectTypePoint(Context context, String type) {
		int i = 0;
		DBHepler dbHepler = new DBHepler(context);
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		String[] columns = new String[] { "id", "name", "other_name", "details", "pic", "xlong", "xlat", "style", "map_style" };
		Cursor c = db.query(DBHepler.T_WORK_TABLE, columns, "style=?", new String[] { type }, null, null, null);
		ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
		while (c.moveToNext()) {
			i++;
			MapPoint item = new MapPoint();
			item.id = c.getString(c.getColumnIndex("id"));
			item.name = c.getString(c.getColumnIndex("name"));
			item.other_name = c.getString(c.getColumnIndex("other_name"));
			item.details = c.getString(c.getColumnIndex("details"));
			item.icon = "map_pop_" + i;
			item.pic = c.getString(c.getColumnIndex("pic"));
			item.xlong = c.getDouble(c.getColumnIndex("xlong"));
			item.xlat = c.getDouble(c.getColumnIndex("xlat"));
			item.style = c.getInt(c.getColumnIndex("style"));
			item.map_style = c.getString(c.getColumnIndex("map_style"));
			mapPoints.add(item);
		}
		c.close();
		db.close();
		dbHepler.close();
		return mapPoints;
	}

	/**
	 * 根据Id查询本地任务
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static MapPoint selectPoint(Context context, String id) {
		int i = 0;
		DBHepler dbHepler = new DBHepler(context);
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		String[] columns = new String[] { "id", "name", "other_name", "details", "pic", "xlong", "xlat", "style", "map_style" };
		Cursor c = db.query(DBHepler.T_WORK_TABLE, columns, "id=?", new String[] { id }, null, null, null);

		MapPoint item = null;
		while (c.moveToNext()) {
			i++;
			item = new MapPoint();
			item.id = c.getString(c.getColumnIndex("id"));
			item.name = c.getString(c.getColumnIndex("name"));
			item.other_name = c.getString(c.getColumnIndex("other_name"));
			item.details = c.getString(c.getColumnIndex("details"));
			item.pic = c.getString(c.getColumnIndex("pic"));
			item.icon = "map_pop_" + i;
			item.xlong = c.getDouble(c.getColumnIndex("xlong"));
			item.xlat = c.getDouble(c.getColumnIndex("xlat"));
			item.style = c.getInt(c.getColumnIndex("style"));
			item.map_style = c.getString(c.getColumnIndex("map_style"));
		}
		c.close();
		db.close();
		dbHepler.close();
		return item;
	}

	/**
	 * 模糊查询匹配
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static ArrayList<MapPoint> selectFuzzyPoint(Context context, String text) {
		int i = 0;
		DBHepler dbHepler = new DBHepler(context);
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		String[] columns = new String[] { "id", "name", "other_name", "details", "pic", "xlong", "xlat", "style", "map_style" };
		Cursor c = db.query(DBHepler.T_WORK_TABLE, columns, "(name like ? or other_name like ? or details like ?)and style!=5", new String[] { "%" + text + "%", "%" + text + "%", "%" + text + "%" },
				null, null, null);
		ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
		while (c.moveToNext()) {
			i++;
			MapPoint item = new MapPoint();
			item.id = c.getString(c.getColumnIndex("id"));
			item.name = c.getString(c.getColumnIndex("name"));
			item.other_name = c.getString(c.getColumnIndex("other_name"));
			item.details = c.getString(c.getColumnIndex("details"));
			item.icon = "map_pop_" + i;
			item.pic = c.getString(c.getColumnIndex("pic"));
			item.xlong = c.getDouble(c.getColumnIndex("xlong"));
			item.xlat = c.getDouble(c.getColumnIndex("xlat"));
			item.style = c.getInt(c.getColumnIndex("style"));
			item.map_style = c.getString(c.getColumnIndex("map_style"));
			mapPoints.add(item);
		}
		c.close();
		db.close();
		dbHepler.close();
		return mapPoints;
	}

}