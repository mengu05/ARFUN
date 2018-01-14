package my.edu.tarc.arfun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DrawActivity extends AppCompatActivity {
    private CanvasView canvasView;
    private static int color = -16777216;
    private static float strokeWidth = 10f;
    private Bitmap mBitmap;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        canvasView = (CanvasView) findViewById(R.id.canvas);
    }

    public void clearCanvas(View v){
        canvasView.clearCanvas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void displayToast(String message) {
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void onPause() {
        if(toast != null)
            toast.cancel();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.redColor:
                canvasView.setPathColor(Color.RED);
                return true;

            case R.id.orangeColor:
                canvasView.setPathColor(Color.rgb(255,165,0));
                return true;

            case R.id.yellowColor:
                canvasView.setPathColor(Color.YELLOW);
                return true;

            case R.id.greenColor:
                canvasView.setPathColor(Color.GREEN);
                return true;

            case R.id.blueColor:
                canvasView.setPathColor(Color.BLUE);
                return true;

            case R.id.indigoColor:
                canvasView.setPathColor(Color.rgb(75,0,130));
                return true;

            case R.id.purpleColor:
                canvasView.setPathColor(Color.rgb(128,0,128));
                return true;

            case R.id.blackColor:
                canvasView.setPathColor(Color.BLACK);
                return true;

            case R.id.enlarge:
                strokeWidth += 10f;
                canvasView.strokeWidth(strokeWidth);
                displayToast("Pencil Size increased");
                return true;

            case R.id.shrink:
                strokeWidth -= 10f;
                canvasView.strokeWidth(strokeWidth);
                displayToast("Pencil Size decreased");
                return true;

            case R.id.eraser:
                canvasView.setPathColor(Color.WHITE);
                displayToast("Eraser selected");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static int getColor(){
        return color;
    }

    public static float getStrokeWidth(){
        return strokeWidth;
    }
}
