package com.example.webservicesdemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl_SecondApp))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServicesEndpoints webServicesEndpoints = retrofit.create(WebServicesEndpoints.class);

        Button btClearResults = findViewById(R.id.btClearResults);
        btClearResults.setOnClickListener((v) -> {
            textViewResult.setText("");
        });

        /**
         * * GetProducts - Vai buscar todos os produtos existentes na DataBase
         */
        Button btGetAllProducts = findViewById(R.id.btGetAllProducts);
        btGetAllProducts.setOnClickListener((v) -> {
            textViewResult.setText("");

            Call<List<Product>> call = webServicesEndpoints.getProducts();
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    if (!response.isSuccessful()) {
                        Toast toast = Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    List<Product> apiResponse = response.body();
                    if (apiResponse != null) {
                        for (Product p : apiResponse) {
                            String content = "";
                            content += "ID: " + p.getId() + "\nName: " + p.getName() + "\n" +
                                    "Price: " + p.getPrice() + "€\n\n";

                            textViewResult.append(content);
                        }
                        Toast toast = Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                    textViewResult.setText(t.getMessage());
                }
            });
        });

        /**
         * * Implementação do GetProductById
         * @param ProductId - Product Identifier
         */
        Button btGetProductById = findViewById(R.id.btGetProductById);
        btGetProductById.setOnClickListener((v) -> {
            textViewResult.setText("");

            EditText productId = findViewById(R.id.editTextProductId);
            if (!productId.getText().toString().equals("")) {

                Call<Product> call = webServicesEndpoints.getProductById(productId.getText().toString());
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        if (!response.isSuccessful()) {
                            Toast toast = Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Product product = response.body();
                        if (product != null && product.getId() != 0) {
                            String content = "ID: " + product.getId() + "\nName: " + product.getName() + "\n" +
                                    "Price: " + product.getPrice() + "€\n\n";
                            textViewResult.append(content);

                            Toast toast = Toast.makeText(MainActivity.this, "Product found!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(MainActivity.this, "No product found!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        productId.setText("");
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        textViewResult.setText(t.getMessage());

                        productId.setText("");
                    }
                });
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "Insert a Product Id!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /**
         * * Implementação do CreateProduct
         * @param Name - Nome do Produto
         * @param Price - Preço do Produto
         * * Devolve o produto criado e um atributo (Success) que indica se o produto foi criado com sucesso
         */
        Button btCreateProduct = findViewById(R.id.btCreateProduct);
        btCreateProduct.setOnClickListener((v) -> {
            textViewResult.setText("");

            EditText productName = findViewById(R.id.editTextProductName);
            EditText productPrice = findViewById(R.id.editTextProductPrice);
            if (!productName.getText().toString().equals("") && !productPrice.getText().toString().equals("")) {
                Product newProduct = new Product(productName.getText().toString(), Double.parseDouble(productPrice.getText().toString()));

                Call<Product> call = webServicesEndpoints.createProduct(newProduct);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        if (!response.isSuccessful()) {
                            Toast toast = Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Product product = response.body();
                        if (product != null && product.getId() != 0) {
                            String content = "ID: " + product.getId() + "\nName: " + product.getName() + "\n" +
                                    "Price: " + product.getPrice() + "€\n\n";
                            textViewResult.append(content);

                            Toast toast = Toast.makeText(MainActivity.this, "Product created!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(MainActivity.this, "Product not created!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        productName.setText("");
                        productPrice.setText("");
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        textViewResult.setText(t.getMessage());

                        productName.setText("");
                        productPrice.setText("");
                    }
                });
            } else {
                Toast toast = Toast.makeText(MainActivity.this, productName.getText().toString().equals("") ? "Insert a valid Name!" : productPrice.getText().toString().equals("") ? "Insert a valid Price!" : "Insert a valid Product!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /**
         * * Implementação do UpdateProduct
         * @param Id - Id do Produto
         * @param Name - Nome do Produto
         * @param Price - Preço do Produto
         * * Devolve o produto atualizado e um atributo (Success) que indica se o produto foi atualizado com sucesso
         */
        Button btUpdateProduct = findViewById(R.id.btUpdateProduct);
        btUpdateProduct.setOnClickListener((v) -> {
            textViewResult.setText("");

            EditText productId = findViewById(R.id.editTextUpdateProductId);
            EditText productName = findViewById(R.id.editTextUpdateProductName);
            EditText productPrice = findViewById(R.id.editTextUpdateProductPrice);
            if (!productId.getText().toString().equals("") && (!productName.getText().toString().equals("") || !productPrice.getText().toString().equals(""))) {
                Call<Product> call = webServicesEndpoints.getProductById(productId.getText().toString());

                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        if (!response.isSuccessful()) {
                            Toast toast = Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Product product = response.body();
                        if (product != null && product.getId() != 0) {
                            Product productUpdated = !productName.getText().toString().equals("") && !productPrice.getText().toString().equals("") ?
                                    new Product(product.getId(), productName.getText().toString(), Double.parseDouble(productPrice.getText().toString())) :
                                    !productName.getText().toString().equals("") ?
                                            new Product(product.getId(), productName.getText().toString(), product.getPrice()) :
                                            !productPrice.getText().toString().equals("") ?
                                                    new Product(product.getId(), product.getName(), Double.parseDouble(productPrice.getText().toString())) :
                                                    new Product();

                            call = webServicesEndpoints.updateProduct(productUpdated);
                            call.enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                                    if (!response.isSuccessful()) {
                                        Toast toast = Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT);
                                        toast.show();
                                        return;
                                    }

                                    Product product = response.body();
                                    if (product != null && product.getId() != 0) {
                                        String content = "ID: " + product.getId() + "\nName: " + product.getName() + "\n" +
                                                "Price: " + product.getPrice() + "€\n\n";
                                        textViewResult.append(content);

                                        Toast toast = Toast.makeText(MainActivity.this, "Product updated!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Toast toast = Toast.makeText(MainActivity.this, "Product not updated!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    productId.setText("");
                                    productName.setText("");
                                    productPrice.setText("");
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    textViewResult.setText(t.getMessage());

                                    productId.setText("");
                                    productName.setText("");
                                    productPrice.setText("");
                                }
                            });
                        } else {
                            Toast toast = Toast.makeText(MainActivity.this, "No product found!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        textViewResult.setText(t.getMessage());

                        productId.setText("");
                        productName.setText("");
                        productPrice.setText("");
                    }
                });
            } else {
                Toast toast = Toast.makeText(MainActivity.this, productName.getText().toString().equals("") ? "Insert a valid Name!" : productPrice.getText().toString().equals("") ? "Insert a valid Price!" : "Insert a valid Product!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /**
         * * Implementação do DeleteProductById
         * @param Id - Id do Produto
         * * Elimina um produto pelo Id e devolve um atributo (Success) que indica se o produto foi eliminado com sucesso
         */
        Button btDeleteProduct = findViewById(R.id.btDeleteProduct);
        btDeleteProduct.setOnClickListener((v) -> {
            textViewResult.setText("");

            EditText productId = findViewById(R.id.editTextDeleteProductId);
            if (!productId.getText().toString().equals("")) {
                Call<Product> call = webServicesEndpoints.deleteProductById(productId.getText().toString());
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        if (!response.isSuccessful()) {
                            Toast toast = Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Product product = response.body();
                        Toast toast;
                        if (product != null && product.getSuccess())
                            toast = Toast.makeText(MainActivity.this, "Product deleted!", Toast.LENGTH_SHORT);
                        else
                            toast = Toast.makeText(MainActivity.this, "No product deleted!", Toast.LENGTH_SHORT);

                        toast.show();

                        productId.setText("");
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        textViewResult.setText(t.getMessage());

                        productId.setText("");
                    }
                });
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "Insert a Product Id!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}