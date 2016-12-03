package com.example.h8951.android_http_request_test;

import java.util.List;

/**
 * Created by Aleksi on 11.11.2016.
 */

public interface JSONResponse {
    void VenuesResponse(List<Venue> venues);
    void UsersResponse(List<User> users);
}
