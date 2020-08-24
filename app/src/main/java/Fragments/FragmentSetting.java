package Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e7gzle.Home;
import com.example.e7gzle.Login;
import com.example.e7gzle.MainActivity;
import com.example.e7gzle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentSetting extends Fragment {

    private TextView full_name;
    private EditText dialog_tv1,dialog_tv2;
    private ImageView btn_full_name,btn_pass,btn_img;
    private CircleImageView image;
    private Button dialog_btn_cancel,dialog_btn_add;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private String data_full_name,dialog_first_name,dialog_last_name,dialog_pass,dialog_con_pass,image_url;
    private Uri uri;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);

        image = view.findViewById(R.id.setting_img);
        full_name = view.findViewById(R.id.setting_full_name);
        btn_full_name = view.findViewById(R.id.setting_btn_full_name);
        btn_pass = view.findViewById(R.id.setting_btn_pass);
        btn_img = view.findViewById(R.id.setting_btn_img);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(mAuth.getUid()+".jpg");

        myRef.child("Users").child(mAuth.getUid()).child("Personal_Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getKey().equals("full_name")) {
                        full_name.setText(snapshot1.getValue().toString());
                    } else if (snapshot1.getKey().equals("Profile_images")) {
                        image_url = snapshot1.getValue().toString();
                        Picasso.get().load(image_url)
                                .placeholder(R.drawable.ic_baseline_account_circle_24_blue)
                                .error(R.drawable.ic_baseline_account_circle_24_blue)
                                .into(image);
                    }
                }

                btn_full_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view = getLayoutInflater().inflate(R.layout.dialog_design, null);
                        dialog_tv1 = view.findViewById(R.id.dialog_text1);
                        dialog_tv2 = view.findViewById(R.id.dialog_text2);
                        dialog_btn_add = view.findViewById(R.id.dialog_btn_add);
                        dialog_btn_cancel = view.findViewById(R.id.dialog_btn_cancel);
                        builder.setView(view);
                        builder.setCancelable(false);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        /* this line to make the border transparent */
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);

                        dialog_tv1.setText(snapshot.child("first_name").getValue().toString());
                        dialog_tv2.setText(snapshot.child("last_name").getValue().toString());
                        // this line to make the cursor at the end of the text
                        dialog_tv1.setSelection(dialog_tv1.getText().length());
                        dialog_tv2.setSelection(dialog_tv2.getText().length());

                        dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog_btn_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!validationText1()) {
                                } else if (!validationText2()) {
                                } else {
                                    dialog_first_name=dialog_tv1.getText().toString();
                                    dialog_last_name=dialog_tv2.getText().toString();
                                    data_full_name=dialog_first_name + " " + dialog_last_name;
                                    myRef.child("Users").child(mAuth.getUid()).child("Personal_Info").child("first_name").setValue(dialog_first_name);
                                    myRef.child("Users").child(mAuth.getUid()).child("Personal_Info").child("last_name").setValue(dialog_last_name);
                                    myRef.child("Users").child(mAuth.getUid()).child("Personal_Info").child("full_name").setValue(data_full_name);
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Your name edited successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                btn_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view = getLayoutInflater().inflate(R.layout.dialog_design, null);
                        dialog_tv1 = view.findViewById(R.id.dialog_text1);
                        dialog_tv2 = view.findViewById(R.id.dialog_text2);
                        dialog_btn_add = view.findViewById(R.id.dialog_btn_add);
                        dialog_btn_cancel = view.findViewById(R.id.dialog_btn_cancel);
                        dialog_tv1.setHint("New Password");
                        dialog_tv2.setHint("Confirm Password");
                        builder.setView(view);
                        builder.setCancelable(false);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        /* this line to make the border transparent */
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);

                        dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog_btn_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!validationPass()) {
                                } else if (!validationCon_pass()) {
                                } else if (!dialog_pass.equals(dialog_con_pass)) {
                                    dialog_tv1.setError("The two passwords should be equals");
                                    dialog_tv2.setError("The two passwords should be equals");
                                } else {
                                    // this line to make the cursor at the end of the text
                                    dialog_tv1.setSelection(dialog_tv1.getText().length());
                                    dialog_tv2.setSelection(dialog_tv2.getText().length());
                                    dialog_pass = dialog_tv1.getText().toString();
                                    mAuth.getCurrentUser().updatePassword(dialog_pass);
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    Toast.makeText(getContext(), "Please login again", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getContext(), Login.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                });

                btn_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Image/*"),1);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private final static Pattern PATTERNNAME = Pattern.compile("[\u0600-\u065F\u066A-\u06EF\u06FA-\u06FFa-zA-Z ]+[\u0600-\u065F\u066A-\u06EF\u06FA-\u06FFa-zA-Z- ]");

    public boolean validationText1() {
        dialog_first_name = dialog_tv1.getText().toString();
        if (dialog_first_name.isEmpty()) {
            dialog_tv1.setError("Empty field");
            dialog_tv1.requestFocus();
            return false;
        } else if (!PATTERNNAME.matcher(dialog_first_name).matches()) {
            dialog_tv1.setError("Please enter alphabet letters only");
            return false;
        } else {
            dialog_tv1.setError(null);
            return true;
        }
    }

    public boolean validationText2() {
        dialog_last_name = dialog_tv2.getText().toString();
        if (dialog_last_name.isEmpty()) {
            dialog_tv2.setError("Empty field");
            dialog_tv2.requestFocus();
            return false;
        } else if (!PATTERNNAME.matcher(dialog_last_name).matches()) {
            dialog_tv2.setError("Please enter alphabet letters only");
            return false;
        } else {
            dialog_tv2.setError(null);
            return true;
        }
    }

    public boolean validationPass() {
        dialog_pass = dialog_tv1.getText().toString();

        if (dialog_pass.isEmpty()) {
            dialog_tv1.setError("Empty field");
            dialog_tv1.requestFocus();
            return false;
        } else if (dialog_pass.length() < 6) {
            dialog_tv1.setError("Password should be more than 5 letters");
            dialog_tv1.requestFocus();
            return false;
        } else {
            dialog_tv1.setError(null);
            return true;
        }
    }

    public boolean validationCon_pass() {
        dialog_con_pass = dialog_tv2.getText().toString();

        if (dialog_con_pass.isEmpty()) {
            dialog_tv2.setError("Empty field");
            dialog_tv2.requestFocus();
            return false;
        } else if (dialog_con_pass.length() < 6) {
            dialog_tv2.setError("Password should be more than 5 letters");
            dialog_tv2.requestFocus();
            return false;
        } else {
            dialog_tv2.setError(null);
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            storageReference.child(mAuth.getUid() + ".jpg");
            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String downloadUrl = task.getResult().toString();
                                myRef.child("Users").child(mAuth.getUid()).child("Personal_Info").child("Profile_images").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                        }else {
                                            String message = task.getException().toString();
                                        }
                                    }
                                });
                            } else {
                            }
                        }
                    });
                }
            });
        }
    }
}
