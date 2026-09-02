package com.termux.boot;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BootActivity extends Activity {

    /** Placeholder in overview.html standing in for the edition this build was made for. */
    private static final String PACKAGE_NAME_PLACEHOLDER = "__TERMUX_PACKAGE_NAME__";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        String overview = readOverview();
        if (overview != null) {
            webView.loadDataWithBaseURL("file:///android_asset/", overview, "text/html", "utf-8", null);
        } else {
            webView.loadUrl("file:///android_asset/overview.html");
        }
        setContentView(webView);
    }

    /** The overview page with the edition's own paths in it, or null if it cannot be read. */
    @Nullable
    private String readOverview() {
        try (InputStream in = getAssets().open("overview.html")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
            return out.toString("utf-8").replace(PACKAGE_NAME_PLACEHOLDER, BuildConfig.TERMUX_PACKAGE_NAME);
        } catch (IOException e) {
            return null;
        }
    }
}
