package com.example.madcampweek2;

import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment {

    protected Interpreter tflite;
    private  int imageSizeX;
    private  int imageSizeY;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;

    public Bitmap oribitmap,testbitmap;
    public static Bitmap cropped;
    Uri imageuri;

    ImageView oriImage,testImage;
    Button buverify;
    TextView result_text;
    ImageView oriImage_cropped;
    ImageView testImage_cropped;

    float[][] ori_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_3, container, false);
        initComponents(rootView);
        return rootView;
    }


    private void initComponents(View rootView) {

        oriImage=(ImageView) rootView.findViewById(R.id.image1);
        testImage=(ImageView)rootView.findViewById(R.id.image2);
        oriImage_cropped = (ImageView)rootView.findViewById(R.id.image3);
        testImage_cropped=(ImageView)rootView.findViewById(R.id.image4);

        buverify=(Button)rootView.findViewById(R.id.verify);
        result_text=(TextView)rootView.findViewById(R.id.result);

        try{
            tflite=new Interpreter(loadmodelfile(getActivity()));
        }catch (Exception e) {
            e.printStackTrace();
        }

        oriImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),12);
            }
        });

        testImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),13);
            }
        });

        buverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double distance = calculate_distance(ori_embedding,test_embedding);

                if(distance<5.8) {
                    result_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.sameFace));
                    result_text.setText("Result : Same Faces"); // \n" + "distance : " + String.valueOf(distance));
                }
                else if(distance>6.2){
                    result_text.setTextColor(ContextCompat.getColor(getActivity(),R.color.differentFace));
                    result_text.setText("Result : Different Faces"); //\n" + "distance : " + String.valueOf(distance));
                }
                else {
                    result_text.setTextColor(ContextCompat.getColor(getActivity(),R.color.similarFace));
                    result_text.setText("Result : Similar Faces"); // \n" + "distance : " + String.valueOf(distance));
                }

            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                oribitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageuri);
                oriImage.setImageBitmap(oribitmap);
                face_detector(oribitmap,"original");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode==13 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                testbitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageuri);
                testImage.setImageBitmap(testbitmap);
                face_detector(testbitmap,"test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void face_detector(final Bitmap bitmap, final String imagetype){


        final InputImage image = InputImage.fromBitmap(bitmap,0);
        FaceDetector detector = FaceDetection.getClient();

        detector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        Face face = faces.get(0);
                        float eulerZ = face.getHeadEulerAngleZ();
                        Matrix matrix = new Matrix();
                        matrix.preRotate(eulerZ);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        final InputImage image2 = InputImage.fromBitmap(rotatedBitmap,0);

                        detector.process(image2)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<Face>>() {
                                            @Override
                                            public void onSuccess(List<Face> faces) {
                                                // Task completed successfully
                                                for (Face face : faces) {
                                                    Rect bounds = face.getBoundingBox();
                                                    cropped = Bitmap.createBitmap(rotatedBitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
                                                    if(imagetype == "original"){
                                                        oriImage_cropped.setImageBitmap(cropped);
                                                    }
                                                    else{
                                                        testImage_cropped.setImageBitmap(cropped);
                                                    }
                                                    get_embaddings(cropped,imagetype);
                                                }
                                            }

                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });

                    }

                });

    }

    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum =0.0;
        for(int i=0;i<128;i++){
            sum=sum+Math.pow((ori_embedding[0][i]-test_embedding[0][i]),2.0);
        }
        return Math.sqrt(sum);
    }

    public void get_embaddings(Bitmap bitmap,String imagetype){

        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];
        //bitmap = rotateCW(bitmap);
        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap,inputImageBuffer);

        tflite.run(inputImageBuffer.getBuffer(),embedding);

        if(imagetype.equals("original"))
            ori_embedding=embedding;
        else if (imagetype.equals("test"))
            test_embedding=embedding;
    }

    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer ) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }


}