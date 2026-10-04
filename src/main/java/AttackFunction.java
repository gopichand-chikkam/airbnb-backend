import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AttackFunction {


    public static void main(String[] args) throws Exception {
        int totalRequests = 10;

        String json = """
                {
                    "hotel_id":1,
                    "room_id":1,
                    "user_id":1,
                    "total_guest":3,
                    "guestDetails":[
                       { "name":"gopi",
                        "gender":"MALE"
                       },
                       {
                        "name":"jagadish",
                        "gender":"MALE"
                       }
                    ],
                
                    "check_in":"10-10-2026",
                    "check_out":"12-10-2026"
                
                }
            """;

        HttpClient client = HttpClient.newHttpClient();

        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        CountDownLatch startLatch =
                new CountDownLatch(1);

        List<Future<Integer>> results =
                new ArrayList<>();

        for (int i = 0; i < totalRequests; i++) {

            Future<Integer> future = executor.submit(() -> {

                // Wait until all 100 threads are ready
                //startLatch.await();



                HttpRequest request =
                        HttpRequest.newBuilder()
                                .uri(URI.create(
                                        "http://localhost:8080/api/v1/admin/payments/startPayment"
                                ))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                .build();

                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

                System.out.println(
                        Thread.currentThread().getName()
                                + " -> "
                                + response.body()+response.statusCode()
                );

                return response.statusCode();
            });

            results.add(future);
        }

        System.out.println("100 requests are ready...");

        // 🚀 RELEASE ALL 100 THREADS
        startLatch.countDown();

        int success = 0;
        int failure = 0;

        for (Future<Integer> result : results) {

            int status = result.get();

            if (status >= 200 && status < 300) {
                success++;
            } else {
                failure++;
            }
        }

        executor.shutdown();

        System.out.println("==========================");
        System.out.println("Total requests = " + totalRequests);
        System.out.println("Successful     = " + success);
        System.out.println("Failed         = " + failure);
        System.out.println("==========================");
    }
}