package com.learnjava.executor;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.concurrent.*;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

/**
 * This class responsible to represents action for Product microservices
 * Shows how to using executorService, its a great scenario for threadPools.
 */
public class ProductServiceUsingExecutor {

    //initialize executor
    static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingExecutor(ProductInfoService productInfoService, ReviewService reviewService) {

        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    /**
     * Method responsbile to find detail by productID.
     * @param productId
     * @return
     */
    public Product retrieveProductDetails(String productId) throws ExecutionException, InterruptedException {

        stopWatch.start(); //start time

        /**
         * In this scenario Im execute service with ExecutorService
         * this point is similar with thread pool. Future is a promise when value is returned
         */
        Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
        Future<Review> reviewFuture = executorService.submit(() -> reviewService.retrieveReviews(productId));

        /**
         * You can parametrize this method with timeout
         */
        //ProductInfo productInfo = productInfoFuture.get(1, TimeUnit.MILLISECONDS);

        ProductInfo productInfo = productInfoFuture.get();
        Review review = reviewFuture.get();

        stopWatch.stop(); //finish time
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingExecutor productService = new ProductServiceUsingExecutor(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

        executorService.shutdown();

    }
}
