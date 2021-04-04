package com.learnjava.service;

import com.learnjava.domain.Review;

import static com.learnjava.util.CommonUtil.delay;

/**
 * Class responsible to represents Microservice for product review
 */
public class ReviewService {

    /**
     * Return Review by product
     * @param productId
     * @return
     */
    public Review retrieveReviews(String productId) {
        delay(1000); // Simulate retrieve
        return new Review(200, 4.5);
    }
}
