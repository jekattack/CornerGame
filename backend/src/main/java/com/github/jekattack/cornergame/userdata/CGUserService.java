package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.userdata.cgUserDetails.Visit;
import com.github.jekattack.cornergame.userdata.cgUserDetails.VisitCreationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserService {

    private final CGUserRepository cgUserRepository;
    private final KioskRepository kioskRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserCreationData userCreationData) {
        if(userCreationData.getUsername()==null || userCreationData.getUsername().isBlank()) throw new IllegalArgumentException("Registration failed: No username set");
        if(userCreationData.getEmail()==null || userCreationData.getEmail().isBlank()) throw new IllegalArgumentException("Registration failed: No email set");
        if(userCreationData.getPassword()==null || userCreationData.getPassword().isBlank()) throw new IllegalArgumentException("Registration failed: No password set");
        if(!(userCreationData.getPassword().equals(userCreationData.getPasswordAgain()))) throw new IllegalArgumentException("Password validation failed: Entered passwords don't match");

        CGUser cgUser = new CGUser(userCreationData.getUsername().toLowerCase(), userCreationData.getEmail(), userCreationData.getPassword());
        cgUser.setPassword(passwordEncoder.encode(cgUser.getPassword()));
        cgUser.setRole("user");
        cgUserRepository.save(cgUser);
    }


    public Optional<CGUser> findByUsername(String username) {
        return cgUserRepository.findByUsername(username.toLowerCase());
    }
    public Optional<CGUser> findById(String id) {
        return cgUserRepository.findById(id);
    }

    public void addVisit(VisitCreationData visitCreationData, String username) {

        //User Coordinates
        CGUser user = cgUserRepository.findByUsername(username).orElseThrow();
        double userLocationLat = visitCreationData.getUserLocation().getUserLocationCoordinates().getLat();
        double userLocationLng = visitCreationData.getUserLocation().getUserLocationCoordinates().getLng();

        //Kiosk Coordinates
        Kiosk kioskToVisit = kioskRepository.findByGooglePlacesId(visitCreationData.getGooglePlacesId()).orElseThrow();
        double kioskToVisitLat = kioskToVisit.getKioskLocation().getLocation().getLat();
        double kioskToVisitLng = kioskToVisit.getKioskLocation().getLocation().getLng();

        //Check if User visited Kiosk within the last 24 hours
        List<Visit> allVisitsAtKioskToVisit = user.getVisits().stream()
                .filter(visits -> visits.getGooglePlacesId().equals(kioskToVisit.getGooglePlacesId())).toList();
        if(!allVisitsAtKioskToVisit.isEmpty()){
            List<Date> timestamps = allVisitsAtKioskToVisit.stream().map(Visit::getIssuedAt).toList();
            Date timestampNow = Date.from(Instant.now());
            for(Date timestamp : timestamps){
                timestamp.setTime(timestamp.getTime() + (24*60*60*1000));
                if(timestampNow.before(timestamp)){
                    throw new IllegalStateException();
                }
            }
        }

        //Check if Users Location is valid for visit
        if(kioskToVisitLat - 0.0001 < userLocationLat
                && userLocationLat < kioskToVisitLat + 0.0001
                && kioskToVisitLng - 0.0001 < userLocationLng
                && userLocationLng < kioskToVisitLng + 0.0001){
            Visit newVisit = new Visit(null, visitCreationData.getGooglePlacesId(), Date.from(Instant.now()));
            user.addVisitToList(newVisit);
            cgUserRepository.save(user);
        }
        throw new IllegalStateException();
    }
}
