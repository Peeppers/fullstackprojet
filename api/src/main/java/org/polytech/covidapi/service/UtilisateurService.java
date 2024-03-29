package org.polytech.covidapi.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.polytech.covidapi.model.Center;
import org.polytech.covidapi.model.Utilisateur;
import org.polytech.covidapi.repository.CenterRepository;
import org.polytech.covidapi.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UtilisateurService implements UserDetailsService{

    @Autowired
    private UtilisateurRepository repository;
    private static Logger log = LoggerFactory.getLogger(UtilisateurService.class);
    private final PasswordEncoder passwordEncoder;
    private CenterRepository centerRepository;
    
    public List<Utilisateur> findAll() {

        return (List<Utilisateur>) repository.findAll();
    }
    
    public Utilisateur save(Utilisateur utilisateur){
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return repository.save(utilisateur);
    }

    public Optional<Utilisateur> findById(int id){
        return repository.findById(id);
    }

    public void delete(int id_utilisateur){
        repository.deleteById(id_utilisateur);
    }

    public List<Utilisateur> findadmin() {

        return repository.findByRole("Admin");
    }
    public List<Utilisateur> findsuperadmin() {

        return repository.findByRole("SuperAdmin");
    }
    public List<Utilisateur> finddoc() {

        return repository.findByRole("Doctor");
    }
    public List<Utilisateur> findpatient() {

        return repository.findByRole("Patient");
    }

    public int max(){

        return repository.findMaxId();

    }

    @Autowired
    public UtilisateurService(final UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.repository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostConstruct
    public void createUserDefault(){
        log.info("Creation du user defaut");
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMail("user");
        utilisateur.setRole("Patient");
        utilisateur.setPassword(passwordEncoder.encode("password"));
        this.repository.save(utilisateur);
        log.info("Creation du user admin");
        Utilisateur admin = new Utilisateur();
        admin.setMail("admin");
        admin.setRole("SuperAdmin");
        admin.setPassword(passwordEncoder.encode("password"));
        this.repository.save(admin);
    }
    
    @Override
    public UserDetails loadUserByUsername(final String mail)
            throws UsernameNotFoundException {
        log.info("recuperation de {}", mail);
    
        Optional<Utilisateur> optionalUtilisateur = repository.findByMail(mail);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            return new User(utilisateur.getMail(), utilisateur.getPassword(), List.of());
        } else {
            throw new UsernameNotFoundException("L'utilisateur" + mail + " n'existe pas");
        }
    }

    public List<Utilisateur> getUserByCenterAndRole(String role, int centerId){
        //Optional<Center> center = centerRepository.findById(centerId);
        return repository.getUserByCenterAndRole(centerId , role);
    }

    public void updateUser(String firstname, String lastname, String mail, String role, int userId, Center center){
        this.repository.updateUser(firstname, lastname, mail, role, userId, center);
    }
}