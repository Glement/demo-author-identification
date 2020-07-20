package com.maximsachok.author_identification_demo.Layouts;

import com.maximsachok.author_identification_demo.Dto.AuthorDto;
import com.maximsachok.author_identification_demo.Dto.ProjectDto;
import com.maximsachok.author_identification_demo.Services.AuthorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AuthorLayout extends VerticalLayout {
    private IntegerField authorIDIntegerField;
    private Button getAuthorButton;
    private Label authorLabel;
    private Label authorIDLabel;
    private Grid<ProjectDto> authorProjectsGrid;
    private AuthorService authorService;
    private Button deleteAuthorButton;
    private Button createAuthorButton;
    private IntegerField projectIDIntegerField;
    private Button projectAddButton;
    private Button refreshAuthorsGrid;
    private Grid<AuthorDto> authorsGrid;

    public AuthorLayout() {
        initAuthorService();
        initAuthorIDLabel();
        initAuthorIdIntegerField();
        initAuthorProjectsGrid();
        initDeleteAuthorButton();
        initGetAuthorButton();
        initCreateAuthorButton();
        initProjectIDIntegerField();
        initProjectAddButton();
        initAuthorLabel();
        initAuthorsGrid();
        initRefreshAuthorsGrid();
        add(authorIDIntegerField, getAuthorButton, deleteAuthorButton, createAuthorButton, authorLabel, authorIDLabel, projectIDIntegerField, projectAddButton, authorProjectsGrid, refreshAuthorsGrid, authorsGrid);
        setSizeFull();
    }

    private void initRefreshAuthorsGrid(){
        refreshAuthorsGrid = new Button("Refresh authors grid", clickEvent ->{
            try {
                authorsGrid.setItems(authorService.getAuthors().get());
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        });
    }

    private void initAuthorsGrid(){
        authorsGrid = new Grid<>();
        authorsGrid.addColumn(AuthorDto::getId).setHeader("Author ID").setSortable(true);
    }

    private void initAuthorIdIntegerField(){
        authorIDIntegerField = new IntegerField();
        authorIDIntegerField.setTitle("Author ID");
        authorIDIntegerField.setLabel("Author ID");
        authorIDIntegerField.setMin(0);
    }
    private void initGetAuthorButton(){
        getAuthorButton = new Button("Get Author", clickEvent -> {
            if(!authorIDIntegerField.isEmpty()){
                try {
                    Optional<AuthorDto> authorDto = authorService.getAuthor(authorIDIntegerField.getValue().longValue());
                    Optional<List<ProjectDto>> projects = authorService.getAuthorProjects(authorIDIntegerField.getValue().longValue());
                    if(authorDto.isPresent())
                        authorIDLabel.setText(authorDto.get().getId().toString());
                    else{
                        Notification.show("Author not found").setPosition(Notification.Position.TOP_CENTER);
                        authorIDLabel.setText("");
                    }
                    if(projects.isPresent())
                        authorProjectsGrid.setItems(projects.get());
                    else
                        authorProjectsGrid.setItems(new ArrayList<>());

                } catch (UnexpectedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initAuthorIDLabel(){
        authorIDLabel = new Label();
        authorIDLabel.setText("");
    }
    private void initAuthorProjectsGrid(){
        authorProjectsGrid = new Grid<>();
        authorProjectsGrid.addColumn(ProjectDto::getId).setHeader("ID").setSortable(true);
        authorProjectsGrid.addColumn(ProjectDto::getNameEn).setHeader("Name");
        authorProjectsGrid.addColumn(ProjectDto::getDescEn).setHeader("Decryption");
        authorProjectsGrid.addColumn(ProjectDto::getKeywords).setHeader("Keywords");
        authorProjectsGrid.addComponentColumn(this::buildRemoveButton);
    }
    private void initAuthorService(){
        authorService = new AuthorService();
    }
    private void initDeleteAuthorButton(){
        deleteAuthorButton = new Button("Delete Author", clickEvent -> {
            try {
                if(!authorIDIntegerField.isEmpty() && authorService.deleteAuthor(authorIDIntegerField.getValue().longValue())){
                    Notification.show("Deleted").setPosition(Notification.Position.TOP_CENTER);
                    authorIDLabel.setText("");
                    authorProjectsGrid.setItems(new ArrayList<>());
                }
            } catch (ExecutionException | UnexpectedException | InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
    private void initCreateAuthorButton(){
        createAuthorButton = new Button("Create Author", clickEvent ->{
        try{
            Long id = authorService.createAuthor(new AuthorDto()).get();
            Notification.show("Author created with ID: "+ id).setPosition(Notification.Position.TOP_CENTER);
            authorIDIntegerField.setValue(id.intValue());
            getAuthorButton.click();
        } catch (InterruptedException | ExecutionException | UnexpectedException e) {
            e.printStackTrace();
        }});
    }

    private Button buildRemoveButton(ProjectDto p) {
        return new Button("Remove Project", clickEvent -> {
            try {
                if(authorService.removeProjectFromAuthor(Long.decode(authorIDLabel.getText()), p.getId()))
                    Notification.show("Removed").setPosition(Notification.Position.TOP_CENTER);
            } catch (ExecutionException | UnexpectedException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                authorProjectsGrid.setItems(authorService.getAuthorProjects(Long.decode(authorIDLabel.getText())).get());
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        });
    }

    private void initProjectIDIntegerField(){
        projectIDIntegerField = new IntegerField("Project ID to add");
    }
    private void initProjectAddButton(){
        projectAddButton = new Button("Add Project", clickEvent -> {
           if(!authorIDLabel.getText().isBlank() && !projectIDIntegerField.isEmpty()){
               try {
                   authorService.addProject(Long.decode(authorIDLabel.getText()),projectIDIntegerField.getValue().longValue());
                   authorProjectsGrid.setItems(authorService.getAuthorProjects(Long.decode(authorIDLabel.getText())).get());
               } catch (ExecutionException | InterruptedException | UnexpectedException e) {
                   e.printStackTrace();
               }
           }
        });
    }

    private void initAuthorLabel(){
        authorLabel = new Label("Author ID");
    }
}
