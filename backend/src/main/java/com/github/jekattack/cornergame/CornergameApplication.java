package com.github.jekattack.cornergame;

import com.github.jekattack.cornergame.placesapi.Coordinates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CornergameApplication {
	public static void main(String[] args) {
		SpringApplication.run(CornergameApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Coordinates coordinates() {
		return new Coordinates(new String[][]{
				{"53.56733", "9.8712"},
				{"53.63049", "9.88562"},
				{"53.61502", "9.97557"},
				{"53.60402", "10.04561"},
				{"53.58283", "10.08956"},
				{"53.58731", "9.94673"},
				{"53.55918", "9.97489"},
				{"53.55632", "10.06072"},
				{"53.5347", "10.10672"},
				{"53.53743", "9.98038"},
				{"53.50408", "10.01952"},
				{"53.53511", "9.91584"}
		});
	}
}
