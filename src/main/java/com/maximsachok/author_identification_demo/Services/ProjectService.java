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

public class ProjectService {
    private String API_PATH = "http://localhost:8080/author-identification/";

    public Optional<ProjectDto> getProject(Long id) throws UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project/"+id);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus()==404)
            return Optional.empty();
        else if (response.getStatus()==200)
            return Optional.of(response.readEntity(ProjectDto.class));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<List<AuthorDto>> getProjectAuthors(Long id) throws UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project/"+id+"/authors");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus()==404)
            return Optional.empty();
        else if (response.getStatus()==200)
            return Optional.of(response.readEntity(new GenericType<List<AuthorDto>>(){}));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<Long> createProject(ProjectDto projectDto) throws UnexpectedException, ExecutionException, InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project");
        Response response = target.request(MediaType.APPLICATION_JSON).buildPost(Entity.json(projectDto)).submit().get();
        if (response.getStatus()==201)
            return Optional.of(response.readEntity(Long.class));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public boolean removeAuthorFromProject(Long projectID, Long authorID) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project/"+projectID+"/author/"+authorID);
        Response response = target.request(MediaType.APPLICATION_JSON).buildDelete().submit().get();
        if(response.getStatus()==200)
            return true;
        else if(response.getStatus()==404)
            return false;
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<ProjectDto> updateProject(ProjectDto projectDto) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project/"+projectDto.getId());
        Response response = target.request(MediaType.APPLICATION_JSON).buildPut(Entity.json(projectDto)).submit().get();
        if(response.getStatus()==200)
            return Optional.of(response.readEntity(ProjectDto.class));
        else if(response.getStatus()==404)
            return Optional.empty();
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public boolean deleteProject(Long projectID) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project/"+projectID);
        Response response = target.request(MediaType.APPLICATION_JSON).buildDelete().submit().get();
        if(response.getStatus()==200)
            return true;
        else if(response.getStatus()==404)
            return false;
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public Optional<List<ProjectDto>> getProjects() throws UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if(response.getStatus()==200)
            return Optional.of(response.readEntity(new GenericType<List<ProjectDto>>(){}));
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }

    public boolean addAuthor(Long projectID, Long authorID) throws ExecutionException, InterruptedException, UnexpectedException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(API_PATH+"project/"+projectID+"/author/"+authorID);
        Response response = target.request(MediaType.APPLICATION_JSON).buildPut(Entity.json("")).submit().get();
        if(response.getStatus()==200)
            return true;
        else if(response.getStatus()==404)
            return false;
        else
            throw new UnexpectedException("Unexpected code: " + response.getStatus());
    }
}
