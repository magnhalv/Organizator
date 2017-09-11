package com.magnhalv.organizator.providers;

import android.content.ContentResolver;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by nomaghal on 31.08.2017.
 */

public class OrganizatorContract {

    private OrganizatorContract() {}

    public static final String CONTENT_AUTHORITY = "com.magnhalv.organizator";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content:/" + CONTENT_AUTHORITY);

    private static final String PATH_TASKS = "tasks";

    public static class Task implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.organizator.tasks";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.organizator.task";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_NAME_TASK_ID = "task_id";

        public static final String COLUMN_NAME_TITLE = "title";

        public static final String COLUMN_NAME_DESCRIPTION = "description";

    }


}
