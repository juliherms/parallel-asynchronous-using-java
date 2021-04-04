package com.learnjava.thread;


import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ProductService;
import com.learnjava.service.ReviewService;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

/**
 * This class responsible to represents action for Product microservices
 * Scenario 1 - Implement using threads
 */
public class ProductServiceUsingThread {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    /**
     * Method responsbile to find detail by productID.
     * @param productId
     * @return
     */
    public Product retrieveProductDetails(String productId) throws InterruptedException {

        stopWatch.start(); //start time

        //implements find productInfo with no blocking call
        ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
        Thread productInfoThread = new Thread(productInfoRunnable);

        ReviewRunnable reviewRunnable = new ReviewRunnable(productId);
        Thread reviewThread = new Thread(reviewRunnable);

        //Start two threads
        productInfoThread.start();
        reviewThread.start();

        //Join threads
        productInfoThread.join();
        reviewThread.join();

        //capture data
        ProductInfo productInfo = productInfoRunnable.getProductInfo();
        Review review = reviewRunnable.getReview();


        stopWatch.stop(); //finish time
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductService productService = new ProductService(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }

    /**
     * This class responsible to implements productInfo with non blocking
     */
    private class ProductInfoRunnable implements Runnable {
        private String productId;
        private ProductInfo productInfo;

        public ProductInfo getProductInfo() {
            return productInfo;
        }

        public ProductInfoRunnable(String productId) {
            this.productId = productId;
        }

        @Override
        public void run() {
            //called service method
            productInfo = productInfoService.retrieveProductInfo(productId);
        }
    }

    /**
     * This class responsible to implements Review with non blocking
     */
    private class ReviewRunnable implements  Runnable {

        private String productId;
        private Review review;

        public ReviewRunnable(String productId){
            this.productId = productId;
        }

        public Review getReview() {
            return review;
        }

        @Override
        public void run() {
            review = reviewService.retrieveReviews(productId);
        }
    }
}
