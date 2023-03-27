package fpt.edu.foodlyapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.view.ChangePasswordActivity;
import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.view.UpdateInfoAccountActivity;
import fpt.edu.foodlyapplication.model.User;
import fpt.edu.foodlyapplication.utils.Sever;
import fpt.edu.foodlyapplication.view.SignInActivity;

public class ProfileFragment extends Fragment {
    public static final String KEY_USER = "EmailUser";
    private static final String TAG = "ProfileFragment";
    private TextView tvFullname, tvEmail;
    private MainActivity mainActivity;
    private ConstraintLayout itemAccoutInfo, itemChangePassword, itemLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        mainActivity = (MainActivity) getActivity();
        setText();

        itemAccoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateInfoAccountActivity.class);
                intent.putExtra(KEY_USER, mainActivity.getKeyUser());
                startActivity(intent);
            }
        });

        itemChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ChangePasswordActivity.class);
                intent.putExtra(KEY_USER, mainActivity.getKeyUser());
                startActivity(intent);
            }
        });

        itemLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), SignInActivity.class));
            }
        });
        return view;
    }

    private void initView(View view) {
        tvFullname = (TextView) view.findViewById(R.id.tvFullName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        itemAccoutInfo = (ConstraintLayout) view.findViewById(R.id.itemAccoutInfo);
        itemChangePassword = (ConstraintLayout) view.findViewById(R.id.itemChangePassword);
        itemLogout = (ConstraintLayout) view.findViewById(R.id.itemLogout);
    }

    private void setText() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Sever.url_get_user_by_email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get string result from sever
                if (response.equals("User Not Exists")) {
                    Log.i(TAG, "User Not Exists");
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        // Get object by email
                        User user = new User();
                        user.setEmail(jsonObject.getString("Email"));
                        user.setFullname(jsonObject.getString("FullName"));
                        user.setPassword(jsonObject.getString("Password"));
                        Log.i(TAG, user.toString());
                        // Set text
                        tvEmail.setText(user.getEmail());
                        tvFullname.setText(user.getFullname());

                    } catch (JSONException e) {
                        Log.i(TAG, e.toString());
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Sever error
                Log.i(TAG, error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Push data (email) to body-parser sever
                HashMap<String, String> params = new HashMap<>();
                params.put("email", mainActivity.getKeyUser());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onResume() {
        setText();
        super.onResume();
    }
}