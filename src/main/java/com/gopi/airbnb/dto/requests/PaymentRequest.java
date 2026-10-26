package com.gopi.airbnb.dto.requests;

import java.util.List;

public record PaymentRequest(Long user_id,Long reservation_id

){}// todo i have to replace this user with Jwt object