package com.example.lap10715.demoweek3.demo_green_dao;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lap10715.demoweek3.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyAdapter extends ArrayAdapter {

    private Context context;
    private List<Note> notes;
    private int layoutItem;
    private CompositeDisposable compositeDisposable;
    private NoteDao noteDao;


    public MyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.layoutItem = resource;
        notes = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();

        SQLiteDatabase sqLiteDatabase = new DaoMaster.DevOpenHelper(context, "NOTE")
                .getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        noteDao = daoSession.getNoteDao();

    }

    public void reloadData() {
        Disposable curDispoable = Observable.create(emitter -> {

            if (noteDao.loadAll().size() == 0) {
                noteDao.insert(new Note(1L, "Note 1", "Noi dung note 1", new Date()));
                noteDao.insert(new Note(2L, "Note 2", "Noi dung note 2", new Date()));
            }

            QueryBuilder queryBuilder = noteDao.queryBuilder();
            queryBuilder.orderDesc(NoteDao.Properties.Date);

            List listNote = queryBuilder.list();

            emitter.onNext(listNote);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listNote -> {
                    MyAdapter.this.notes = (List<Note>) listNote;

                    setNotifyOnChange(true);
                    notifyDataSetChanged();
                });

        compositeDisposable.add(curDispoable);
    }

    public void unsubscribe() {
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(layoutItem, null);
        TextView tvTitle = row.findViewById(R.id.tv_title);
        TextView tvContent = row.findViewById(R.id.tv_content);
        TextView tvDate = row.findViewById(R.id.tv_date);

        tvTitle.setText(notes.get(position).getTitle());
        tvContent.setText(notes.get(position).getContent());

        tvDate.setText(new SimpleDateFormat("MM-dd-yyyy")
                .format(notes.get(position).getDate()));
        return row;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    public void deleteNote(int position) {
        Disposable curDispoable = Observable.create(emitter -> {

            Note deleteNote = this.notes.get(position);
            noteDao.delete(deleteNote);

            QueryBuilder queryBuilder = noteDao.queryBuilder();
            queryBuilder.orderDesc(NoteDao.Properties.Date);

            List listNote = queryBuilder.list();

            emitter.onNext(listNote);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listNote -> {
                    MyAdapter.this.notes = (List<Note>) listNote;

                    setNotifyOnChange(true);
                    notifyDataSetChanged();
                });

        compositeDisposable.add(curDispoable);

    }

    public void insertNote(String title, String content, Date curDate) {
        Disposable curDispoable = Observable.create(emitter -> {

            noteDao.insert(new Note(null, title, content, curDate));

            QueryBuilder queryBuilder = noteDao.queryBuilder();
            queryBuilder.orderDesc(NoteDao.Properties.Date);

            List listNote = queryBuilder.list();

            emitter.onNext(listNote);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listNote -> {
                    MyAdapter.this.notes = (List<Note>) listNote;

                    setNotifyOnChange(true);
                    notifyDataSetChanged();
                });

        compositeDisposable.add(curDispoable);
    }

    public void modifyNote(String title, String content, Date curDate, int curPosChange) {
        Disposable curDispoable = Observable.create(emitter -> {

            Note curNote = notes.get(curPosChange);
            curNote.setTitle(title);
            curNote.setContent(content);
            curNote.setDate(curDate);

            noteDao.update(curNote);

            QueryBuilder queryBuilder = noteDao.queryBuilder();
            queryBuilder.orderDesc(NoteDao.Properties.Date);

            List listNote = queryBuilder.list();

            emitter.onNext(listNote);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listNote -> {
                    MyAdapter.this.notes = (List<Note>) listNote;

                    setNotifyOnChange(true);
                    notifyDataSetChanged();
                });

        compositeDisposable.add(curDispoable);
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

}
