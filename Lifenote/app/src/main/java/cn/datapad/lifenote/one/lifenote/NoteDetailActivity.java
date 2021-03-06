package cn.datapad.lifenote.one.lifenote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {

    private StringBuilder mydbcontent;
    private SQLiteDatabase mydb;
    private DbOpenHelper myDBHelper;
    private Context mContext;

    private int fenlei_id = 0;
    private String[] leibie_list = new String[]{"生活","旅游","学习","生日","纪念日"};
    private int[] fenbie_icon_list = new int[]{R.mipmap.iconfont_apartment,R.mipmap.iconfont_gift,
            R.mipmap.iconfont_heart,R.mipmap.iconfont_train,R.mipmap.iconfont_magnifier};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);


        Intent intent = getIntent();
        //从Intent当中根据key取得value
        int note_id = intent.getExtras().getInt("Intent_note_id");
        note_id = note_id + 1;
        String note_id_string = String.valueOf(note_id);
        //note_id_string = String.format();
        //Toast.makeText(note_id, Toast.LENGTH_SHORT).show();
        mContext = NoteDetailActivity.this;
        myDBHelper = new DbOpenHelper(mContext, "note.db", null, 1);
        mydb = myDBHelper.getWritableDatabase();
        mydbcontent = new StringBuilder();

        //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，进一步约束
        // Cursor cursor = mydb.query("note", new String[]{"noteid"}, "noteid like ?", new String[]{note_id}, null, null, null);
        Cursor cursor = mydb.rawQuery("SELECT * FROM note WHERE noteid = ?", new String[]{note_id_string});
        //存在数据才返回true
        int noteid = 1;
        String title = null;
        String date = null;
        String fenlei = null;
        int tianshu = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date_start = new Date(System.currentTimeMillis());//获取当前时间
        Date date_end = null;
        if (cursor.moveToFirst()) {
            do {
                noteid = cursor.getInt(cursor.getColumnIndex("noteid"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                fenlei = cursor.getString(cursor.getColumnIndex("fenlei"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                try {
                    date_end = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        tianshu=getGapCount(date_start,date_end);//获取时间差
        String cha = Integer.toString(tianshu);//将int转化为stirng

        int fenlei_id = Arrays.asList(leibie_list).indexOf(fenlei);//获取图标分类索引

        TextView note_detail_number = (TextView) findViewById(R.id.textView_note_detail_number);
        TextView note_detail_title = (TextView) findViewById(R.id.textView_note_detail_title);
        TextView note_detail_fenlei = (TextView) findViewById(R.id.textView_note_detail_fenlei);
        //ImageView note_detail_icon = (ImageView) findViewById(R.id.imageView_icon);

        note_detail_number.setText(cha);
        note_detail_title.setText(title);
        note_detail_fenlei.setText(fenlei);
        //note_detail_icon.setImageResource(fenbie_icon_list[fenlei_id]);

        note_detail_number.setVisibility(View.VISIBLE);
        note_detail_title.setVisibility(View.VISIBLE);
        note_detail_fenlei.setVisibility(View.VISIBLE);
        //note_detail_icon.setVisibility(View.VISIBLE);
        //编辑，分享，返回点击监听
        ImageView image_share = (ImageView) findViewById(R.id.imageView_share);
        ImageView image_redit = (ImageView) findViewById(R.id.imageView_redit);
        ImageView image_back = (ImageView) findViewById(R.id.imageView_back);
        //ImageView image_delete = (ImageView) findViewById(R.id.imageView_delete);
        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myShare();
            }
        });
        final int finalNoteid = noteid;
        image_redit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myRedit(finalNoteid);
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                NoteDetailActivity.this.finish();
            }
        });
        /*
        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mydialog(finalNoteid);
            }
        });
        */
    }
    public void mydialog(final int finalNoteid){
        new AlertDialog.Builder(NoteDetailActivity.this).setTitle("确认删除？?")
                .setMessage("删除后无法恢复").setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mydelete(finalNoteid);
                    }
                }).show();
    }
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }
    public void mydelete(int finalNoteid){
        mydb = myDBHelper.getWritableDatabase();
        mydbcontent = new StringBuilder();
        String note_id_string = String.valueOf(finalNoteid);//int转String

        mydb.delete("note", "noteid = ?", new String[]{note_id_string});

        Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(NoteDetailActivity.this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        NoteDetailActivity.this.finish();
    }

    public void myShare(){
        Intent shareIntent = new Intent();
        //shareIntent.setAction(Intent.ACTION_SEND);
        //shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
       // shareIntent.setType("text/plain");
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Lifenote,记录点滴生活！");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
    public void myRedit(int finalNoteid){
        Intent reditIntent = new Intent();
        reditIntent.putExtra("Intent_note_id_redit", finalNoteid);
        //Toast.makeText(mContext, "添加成功"+finalNoteid, Toast.LENGTH_SHORT).show();
        reditIntent.setClass(this, NoteReditActivity.class);
        //通过Intent对象启动另外一个Activity
        NoteDetailActivity.this.startActivity(reditIntent);

    }
}
