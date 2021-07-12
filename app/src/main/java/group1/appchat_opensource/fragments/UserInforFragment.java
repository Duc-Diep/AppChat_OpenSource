package group1.appchat_opensource.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;

import group1.appchat_opensource.R;
import group1.appchat_opensource.activity.AuthActivity;
import group1.appchat_opensource.databinding.UserInforFragmentBinding;
import group1.appchat_opensource.dialogs.ChangePasswordDialog;
import group1.appchat_opensource.objects.User;

import static android.app.Activity.RESULT_OK;
import static group1.appchat_opensource.configs.Constant.IMG_LINK_MALE_DEFAULT;

public class UserInforFragment extends Fragment {
    private static final int MY_REQUEST_CODE = 10;
    UserInforFragmentBinding binding;
    User user;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference data;
    Bitmap bitmap;
    Uri filepath;
    UploadTask uploadTask;
    public static UserInforFragment newInstance() {

        Bundle args = new Bundle();

        UserInforFragment fragment = new UserInforFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.user_infor_fragment,container,false );
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        getCurrentUserInfor();

        binding.btnEdit.setOnClickListener(v->{
            binding.edtUsernameInfor.setEnabled(true);
            binding.btnEdit.setColorFilter(Color.YELLOW);
        });
        binding.layoutUserInfo.setOnClickListener(v->{
            binding.edtUsernameInfor.setEnabled(false);
            binding.btnEdit.setColorFilter(Color.BLUE);
            changeName();
        });
        binding.btnBack.setColorFilter(Color.BLUE);
        binding.btnBack.setOnClickListener(v->{
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStackImmediate();
        });
        binding.btnSignOut.setOnClickListener(v->signOut());

        binding.imgChooseImg.setOnClickListener(v->{chooseImage();});
        binding.btnChangePassword.setOnClickListener(v -> {
            updatePassword();
        });
        return binding.getRoot();
    }
    private void getCurrentUserInfor() {
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    String userImagelink = user.getImage_url();
                    String username = user.getUsername();
                    if (getContext()!=null) {
                        if (userImagelink.equalsIgnoreCase("")){
                            Glide.with(getContext()).load(IMG_LINK_MALE_DEFAULT).into(binding.imgAvatarUser);
                        }else
                            Glide.with(getContext()).load(userImagelink).into(binding.imgAvatarUser);
                    }
                    binding.edtUsernameInfor.setText(username);
                    binding.txtEmail.setText("Email:"+firebaseUser.getEmail());
                    binding.txtGender.setText("Gender:"+user.getGender());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loaf Use Information", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void changeName() {
        data  = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("username",binding.edtUsernameInfor.getText().toString());
        data.updateChildren(hm);
    }

    private void updatePassword(){
        ChangePasswordDialog dialog = new ChangePasswordDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void chooseImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 1:
                    if (data==null){
                        Toast.makeText(getContext(), "Choose image failed", Toast.LENGTH_SHORT).show();
                        return;
                    }else  {
                        try {
                            filepath = data.getData();
                            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                            inputStream = getContext().getContentResolver().openInputStream(filepath);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();
                            uploadFile(bitmap);
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        }
    }

    public void signOut(){
        setStatus("Offline");
        mAuth.signOut();
        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivity(intent);
     /*   EventBus.getDefault().post(new EventCloseActivity());*/
    }
    public void setStatus(String status){
        try {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            mAuth = FirebaseAuth.getInstance();
            data = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String,Object> hm = new HashMap<>();
            hm.put("status",status);
            data.updateChildren(hm);
        }catch (Exception e){

        }
    }
    //up ảnh bằng bitmap
    private void uploadFile(Bitmap bitmap) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Uploading Image");
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageref = storage.getReference().child("images/" + firebaseUser.getUid() + ".jpg");
        uploadTask =imageref.putFile(filepath);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setImageurl(uri);
                        Log.d("TAG", "onSuccess: "+uri);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded: " + (int)percent + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to upload", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void setImageurl(Uri uri){
        data  = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("image_url",uri.toString());
        data.updateChildren(hm);
        Glide.with(getContext()).load(uri.toString()).into(binding.imgAvatarUser);
    }
}

