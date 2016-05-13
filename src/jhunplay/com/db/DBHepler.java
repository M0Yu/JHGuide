package jhunplay.com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author chenws 数据库帮助类
 */
public class DBHepler extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "jhun_play.db";
	private static final int DATABASE_VERSION = 1;
	public static final String T_WORK_TABLE = "jhun_play_map";
	public static final String T_WORK_LOG = "jhun_play_log";
	public static final String T_WORK_DO = "jhun_play_do";

	public DBHepler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ T_WORK_TABLE
				+ "(id integer primary key autoincrement,"
				+ "name text , details text, pic text, other_name text ,xlong double ,xlat double , style integer , map_style text)");

		// db.execSQL("create table "
		// + T_WORK_DO
		// + "(id integer primary key autoincrement,"
		// +
		// "taskid text , isSignIn text ,isSignOut text ,isAddpostil text , isCheck text , isSave text)");
		db.execSQL("CREATE TABLE notification (id integer primary key autoincrement,msg_id varchar(64),title varchar(128),activity varchar(256),notificationActionType varchar(512),content text,update_time varchar(16))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + T_WORK_LOG);
		db.execSQL("DROP TABLE IF EXISTS " + T_WORK_DO); // 1.2版本删除该表
		onCreate(db);
	}

}
