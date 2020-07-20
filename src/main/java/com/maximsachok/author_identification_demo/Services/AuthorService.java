package com.maximsachok.author_identification_demo.Services;

import com.maximsachok.author_identification_demo.Dto.AuthorDto;
import com.maximsachok.author_identification_demo.Dto.ProjectDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class AuthorService {

    private String API_PATH = "http://localhost:8080/author-identification/";

    public Optional<AuthorDto> getAuthor(Long id) throws UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author/"+id);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus()==404)
            return Optional.empty();
        else if (response.getStatus()==200)
            return Optional.of(response.readEntity(AuthorDto.class));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<List<ProjectDto>> getAuthorProjects(Long id) throws UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author/"+id+"/projects");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus()==404)
            return Optional.empty();
        else if (response.getStatus()==200)
            return Optional.of(response.readEntity(new GenericType<List<ProjectDto>>(){}));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<Long> createAuthor(AuthorDto authorDto) throws UnexpectedException, ExecutionException, InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author");
        Response response = target.request(MediaType.APPLICATION_JSON).buildPost(Entity.json(authorDto)).submit().get();
        if (response.getStatus()==201)
            return Optional.of(response.readEntity(Long.class));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public boolean removeProjectFromAuthor(Long authorID, Long projectID) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author/"+authorID+"/project/"+projectID);
        Response response = target.request(MediaType.APPLICATION_JSON).buildDelete().submit().get();
        if(response.getStatus()==200)
            return true;
        else if(response.getStatus()==404)
            return false;
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public boolean deleteAuthor(Long authorID) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author/"+authorID);
        Response response = target.request(MediaType.APPLICATION_JSON).buildDelete().submit().get();
        if(response.getStatus()==200)
            return true;
        else if(response.getStatus()==404)
            return false;
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
}

    public boolean addProject(Long authorID, Long projectID) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author/"+authorID+"/project/"+projectID);
        Response response = target.request(MediaType.APPLICATION_JSON).buildPut(Entity.json("")).submit().get();
        if(response.getStatus()==200)
            return true;
        else if(response.getStatus()==404)
            return false;
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<List<AuthorDto>> getAuthors() throws UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"author");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if(response.getStatus()==200)
            return Optional.of(response.readEntity(new GenericType<List<AuthorDto>>(){}));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }
    /*public List<AuthorDto> getAuthors(){
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/authors");
        List<AuthorDto> list = new ArrayList<>();
        return target.request(MediaType.APPLICATION_JSON).get(list.getClass());
    }*/
}