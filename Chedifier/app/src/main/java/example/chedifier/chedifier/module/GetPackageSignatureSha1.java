package example.chedifier.chedifier.module;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import example.chedifier.base.utils.FileUtils;
import example.chedifier.base.utils.StringUtils;
import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/3/22.
 */

public class GetPackageSignatureSha1 extends AbsModule {

    private EditText mInput;
    private TextView mResult;

    public GetPackageSignatureSha1(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


//        LinearLayout contain1 = new LinearLayout(mContext);
//        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        linearLayout.addView(contain1,lp1);

        mInput = new EditText(mContext);
        mInput.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mInput.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        linearLayout.addView(mInput);

        mResult = new TextView(mContext);
        mResult.setText("get signature sha1");
        mResult.setTextIsSelectable(true);
        mResult.setOnClickListener(this);
        linearLayout.addView(mResult);

        return linearLayout;
    }

    @Override
    public void onClick(View v) {
        if(v == mResult){
            String packageName = mInput.getText().toString();

            String sha1 = getCertificateSHA1Fingerprint(packageName);
            if(!StringUtils.isEmpty(sha1)){
                mResult.setText(sha1);

                File f = FileUtils.createFile("/sdcard/temp.txt");
                if(f != null && f.isFile()){
                    try {
                        example.chedifier.chedifier.utils.FileUtils.writeTextFile(f,new String[]{sha1},false);
                        Toast.makeText(mContext,"save to /sdcard/temp.txt success!",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                mResult.setText("failed,retry.");
            }
        }
    }

    private String getCertificateSHA1Fingerprint(String packageName) {
        PackageManager pm = mContext.getPackageManager();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(packageInfo == null){
            return null;
        }

        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

}
