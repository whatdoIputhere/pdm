package com.example.webservicesdemo;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServicesEndpoints {
    @GET("GetProducts")
    Call<List<Product>> getProducts();

    @GET("GetProductById/{ProductId}")
    Call<Product> getProductById(@Path("ProductId") String ProductId);

    @POST("CreateProduct")
    Call<Product> createProduct(@Body Product product);

    @PUT("UpdateProduct")
    Call<Product> updateProduct(@Body Product product);

    @DELETE("DeleteProductById/{ProductId}")
    Call<Product> deleteProductById(@Path("ProductId") String ProductId);
}
