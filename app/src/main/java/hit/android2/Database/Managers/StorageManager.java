package hit.android2.Database.Managers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class StorageManager {

    public interface Listener{
        void onSuccess();
    }

    public interface DataListener<String>{
        void onSuccess(String url);
    }

    static public void uploadImageFromImageview(ImageView imageView){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = "images/profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".png";


        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();

        final StorageReference storageReff = storage.getReference(path);

        UploadTask uploadTask = storageReff.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Log.d("StorageManager",uri.toString());

                        DatabaseManager.updateProfileImage(FirebaseAuth.getInstance().getCurrentUser().getUid(),uri.toString());

                    }
                });

            }
        });
    }

    static public void uploadImageFromImageviewReturnUrl(ImageView imageView, DataListener<String> listener){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = "images/profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".png";


        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();

        final StorageReference storageReff = storage.getReference(path);

        UploadTask uploadTask = storageReff.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Log.d("StorageManager",uri.toString());
                        listener.onSuccess(uri.toString());

                    }
                });

            }
        });
    }
}
