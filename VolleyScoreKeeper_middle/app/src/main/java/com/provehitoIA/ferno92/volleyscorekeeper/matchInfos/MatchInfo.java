package com.provehitoIA.ferno92.volleyscorekeeper.matchInfos;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.provehitoIA.ferno92.volleyscorekeeper.R;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.GameListInfo;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.GameListInfoFragment;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.MainActivity;
import com.provehitoIA.ferno92.volleyscorekeeper.match.VolleyMatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 31/10/2016.
 */

public class MatchInfo extends AppCompatActivity {
    ArrayList<String> lineUpA = new ArrayList<String>();
    ArrayList<String> lineUpB = new ArrayList<String>();
    EditText teamAEditText;
    EditText teamBEditText;
    ImageView missingAName;
    ImageView missingBName;

    String teamAName;
    String teamBName;
    Boolean isLoadingImageA = false;
    byte[] mImageA;
    byte[] mImageB;
    boolean mHasGPSpermission;
    ArrayList<String> mPositions = new ArrayList<>();
    Location mLastKnownLocation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_infos);
        if (getIntent().getExtras() != null) {
            setTeamName();
            lineUpA = getIntent().getExtras().getStringArrayList("lineUpA");
            lineUpB = getIntent().getExtras().getStringArrayList("lineUpB");
            if (lineUpA.size() == 6 && lineUpB.size() == 6) {
                // show different court image maybe with some effects
                ImageView emptyCourt = (ImageView) findViewById(R.id.empty_court);
                emptyCourt.setImageResource(R.drawable.volleyball_court_done);
            }
        }
        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if (h > 600 && w > 600) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            LinearLayout editLineupButtonWrapper = (LinearLayout) findViewById(R.id.lineup_button_wrapper);
            ImageView emptyCourtImage = (ImageView) findViewById(R.id.empty_court);
            emptyCourtImage.setVisibility(View.GONE);
            editLineupButtonWrapper.setVisibility(View.GONE);

            ImageView courtWithNumberImage = (ImageView) findViewById(R.id.court_with_number_info);
            courtWithNumberImage.setVisibility(View.VISIBLE);
            ImageView courtWithNumber = (ImageView) findViewById(R.id.court_with_number);
            courtWithNumber.setVisibility(View.GONE);
            Button editedLineupButton = (Button) findViewById(R.id.lineup_edited_button);
            editedLineupButton.setVisibility(View.GONE);

        } else {
            //Portrait (in all device) or landscape in mobile only
        }
        setLogos();
    }

    public void editLineUp(View v) {
        saveTeamsName();
        if (teamAEditText.getText().toString().isEmpty() == false && teamBEditText.getText().toString().isEmpty() == false) {
            Intent intent = new Intent(MatchInfo.this, EditLineUp.class);
            intent.putExtra("teamA", teamAEditText.getText().toString());
            intent.putExtra("teamB", teamBEditText.getText().toString());
            intent.putExtra("lineUpA", lineUpA);
            intent.putExtra("lineUpB", lineUpB);
            startActivity(intent);
        } else {
            showErrors();
        }
    }

    public void startNewMatch(View v) {
        saveTeamsName();

        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if (h < w && (h > 600 || w > 600)) {
            getLineUp();
        }

        if (teamAEditText.getText().toString().isEmpty() == false && teamBEditText.getText().toString().isEmpty() == false) {

            // Alert if no lineUp
            if (lineUpA.size() < 6 || lineUpB.size() < 6) {
                AlertDialog.Builder alertLineUp = new AlertDialog.Builder(this);
                alertLineUp.setTitle("Line Up");
                alertLineUp.setMessage("You haven't set the team's line-up. " +
                        "You won't be able to track your team's positions until you set them. " +
                        "Do you still want to continue and start the match?");

                alertLineUp.setCancelable(false);
                alertLineUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = createIntent(false);
                        startActivity(intent);
                    }
                });

                alertLineUp.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog alert = alertLineUp.create();
                alert.show();
            } else {
                Intent intent = createIntent(true);
                startActivity(intent);
            }

        } else {
            //Show error
            showErrors();
        }
    }

    private Intent createIntent(boolean withLineUp) {
        Intent intent = new Intent(MatchInfo.this, VolleyMatch.class);
        intent.putExtra("teamA", teamAEditText.getText().toString());
        intent.putExtra("teamB", teamBEditText.getText().toString());
        intent.putExtra("logoA", mImageA);
        intent.putExtra("logoB", mImageB);
        intent.putExtra("positions", mPositions);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK);

        if (withLineUp) {
            intent.putExtra("lineUpA", lineUpA);
            intent.putExtra("lineUpB", lineUpB);
        }
        return intent;
    }

    public void saveTeamsName() {
        teamAEditText = (EditText) findViewById(R.id.edit_team_a);
        teamBEditText = (EditText) findViewById(R.id.edit_team_b);
    }

    public void showErrors() {
        missingAName = (ImageView) findViewById(R.id.missing_a_name);
        missingBName = (ImageView) findViewById(R.id.missing_b_name);
        missingAName.setVisibility(View.INVISIBLE);
        missingBName.setVisibility(View.INVISIBLE);

        if (teamAEditText.getText().toString().isEmpty()) {
            missingAName.setVisibility(View.VISIBLE);
        }
        if (teamBEditText.getText().toString().isEmpty()) {
            missingBName.setVisibility(View.VISIBLE);
        }
        Context context = getApplicationContext();
        CharSequence text = "Missing team name";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void setTeamName() {
        teamAName = getIntent().getExtras().getString("teamA");
        teamBName = getIntent().getExtras().getString("teamB");

        EditText teamANameEdit = (EditText) findViewById(R.id.edit_team_a);
        teamANameEdit.setText(teamAName, TextView.BufferType.EDITABLE);
        EditText teamBNameEdit = (EditText) findViewById(R.id.edit_team_b);
        teamBNameEdit.setText(teamBName, TextView.BufferType.EDITABLE);
    }

    public int getDeviceHeight() {
        //Get height of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.heightPixels / density);
    }

    public int getDeviceWidth() {
        //Get width of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.widthPixels / density);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if (h > w && (h > 600 || w > 600)) {
            getLineUp();
        }
        outState.putStringArrayList("lineUpA", lineUpA);
        outState.putStringArrayList("lineUpB", lineUpB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        lineUpA = savedInstanceState.getStringArrayList("lineUpA");
        lineUpB = savedInstanceState.getStringArrayList("lineUpB");
        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if (h < w && (h > 600 || w > 600)) {
            setPreviousLineUp();
        }

    }

    public void setPreviousLineUp() {

        if (lineUpA.size() != 0 || lineUpB.size() != 0) {
            LinearLayout teamA = (LinearLayout) findViewById(R.id.team_a);
            for (int i = 0; i < teamA.getChildCount(); i++) {
                if (teamA.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                            EditText teamAElement = (EditText) linearLayoutChild.getChildAt(j);
                            teamAElement.setText(lineUpA.get(i - 1), TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }

            LinearLayout teamB = (LinearLayout) findViewById(R.id.team_b);
            for (int i = 0; i < teamB.getChildCount(); i++) {
                if (teamB.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                            EditText teamBElement = (EditText) linearLayoutChild.getChildAt(j);
                            teamBElement.setText(lineUpB.get(i - 1), TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        }
    }

    public void getLineUp() {
        lineUpA.clear();
        lineUpB.clear();
        LinearLayout teamA = (LinearLayout) findViewById(R.id.team_a);
        if (teamA != null) {
            for (int i = 0; i < teamA.getChildCount(); i++) {
                if (teamA.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                            if (((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false) {
                                lineUpA.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                            }
                        }
                    }
                }
            }

            LinearLayout teamB = (LinearLayout) findViewById(R.id.team_b);
            for (int i = 0; i < teamB.getChildCount(); i++) {
                if (teamB.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                            if (((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false) {
                                lineUpB.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                            }
                        }
                    }
                }
            }
        }
    }

    private void setLogos() {
        ImageView teamLogoA = (ImageView) findViewById(R.id.team_a_logo);
        ImageView teamLogoB = (ImageView) findViewById(R.id.team_b_logo);
        teamLogoA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isLoadingImageA = true;
                getPicture();
            }
        });
        teamLogoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isLoadingImageA = false;
                getPicture();
            }
        });
    }

    private void getPicture() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Set logo");
        builder.setMessage("Take a photo or get a picture from library?");
        builder.setNeutralButton("Photo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
            }
        });
        builder.setPositiveButton("Picture", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageView;
        if (isLoadingImageA) {
            imageView = (ImageView) findViewById(R.id.team_a_logo);
        } else {
            imageView = (ImageView) findViewById(R.id.team_b_logo);
        }
        Bitmap photo = null;
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(photo);

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (photo != null) {
            photo = getResizedBitmap(photo, 100);
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            if (isLoadingImageA) {
                mImageA = stream.toByteArray();
            } else {
                mImageB = stream.toByteArray();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void getLocation(View v) {
        if (mHasGPSpermission) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            String locationProvider = LocationManager.NETWORK_PROVIDER;
//            String locationProvider = LocationManager.GPS_PROVIDER;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            } else {
                mLastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                mPositions.clear();
                if (mLastKnownLocation == null) {
                    getLocation(null);
                } else {
                    mPositions.add(String.valueOf(mLastKnownLocation.getLatitude()));
                    mPositions.add(String.valueOf(mLastKnownLocation.getLongitude()));
                    Context context = getApplicationContext();
                    CharSequence text = "Gym location saved";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mHasGPSpermission = true;
                    getLocation(null);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
