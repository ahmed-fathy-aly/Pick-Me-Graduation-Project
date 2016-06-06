package com.asu.pick_me_graduation_project.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.model.CarDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 6/6/2016.
 */
public class CarDetailsView extends LinearLayout
{
    /* UI */
    @Bind(R.id.editTextCarModel)
    EditText editTextCarModel;
    @Bind(R.id.editTextcarYear)
    EditText editTextcarYear;
    @Bind(R.id.editTextCarPlate)
    EditText editTextCarPlate;
    @Bind(R.id.checkBoxAirConditioned)
    CheckBox checkBoxAirConditioned;

    /* constructors */
    public CarDetailsView(Context context)
    {
        super(context);
        init();
    }

    public CarDetailsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CarDetailsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        // inflate layout
        inflate(getContext(), R.layout.view_car_details, this);
        ButterKnife.bind(this);

    }

    /* methods */

    /**
     * updates the UI to these car details
     */
    public void setCarDetails(CarDetails carDetails)
    {
        editTextCarModel.setText(ValidationUtils.correct(carDetails.getModel()));
        editTextcarYear.setText(ValidationUtils.correct(carDetails.getYear()));
        editTextCarPlate.setText(ValidationUtils.correct(carDetails.getPlateNumber()));
        checkBoxAirConditioned.setChecked(carDetails.isConditioned());
    }

    /**
     * calls the API to download the user's car details
     */
    public void downloadCarDetails()
    {
        UserApiController controller = new UserApiController(getContext().getApplicationContext());
        controller.getProfile(new AuthenticationAPIController(getContext()).getCurrentUser().getUserId(), new GetProfileCallback()
        {
            @Override
            public void success(User user)
            {

                editTextCarModel.setText(ValidationUtils.correct(user.getCarDetails().getModel()));
                editTextcarYear.setText(ValidationUtils.correct(user.getCarDetails().getYear()));
                editTextCarPlate.setText(ValidationUtils.correct(user.getCarDetails().getPlateNumber()));
                checkBoxAirConditioned.setChecked(user.getCarDetails().isConditioned());

            }

            @Override
            public void fail(String message)
            {
                Log.e("Game", "error downloading profile " + message);
            }
        });
    }

    /**
     * gathers the data from the UI
     */
    public CarDetails getCarDetails()
    {
        CarDetails carDetails = new CarDetails();

        carDetails.setModel(editTextCarModel.getText().toString());
        carDetails.setPlateNumber(editTextCarPlate.getText().toString());
        carDetails.setYear(editTextcarYear.getText().toString());
        carDetails.setConditioned(checkBoxAirConditioned.isChecked());
        return carDetails;
    }

    /**
     * checks that all the car details werer entered
     * @return
     */
    public boolean checkDataEntered()
    {
        boolean valid = true;

        EditText[] editTexts = {editTextCarModel, editTextCarPlate, editTextcarYear};
        for (EditText editText : editTexts)
            if (editText.getText().toString().trim().length() == 0)
            {
                editText.setError("");
                valid = false;
            }

        return valid;
    }
}
