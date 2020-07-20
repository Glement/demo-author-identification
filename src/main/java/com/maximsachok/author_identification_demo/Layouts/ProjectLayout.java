package com.maximsachok.author_identification_demo.Layouts;

import com.maximsachok.author_identification_demo.Dto.AuthorDto;
import com.maximsachok.author_identification_demo.Dto.ProjectDto;
import com.maximsachok.author_identification_demo.Services.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class ProjectLayout extends VerticalLayout {
    private IntegerField projectIDIntegerField;
    private IntegerField authorIDIntegerField;
    private TextField projectNameTextField;
    private TextField projectDescriptionTextField;
    private TextField projectKeywordsTextField;
    private Button deleteProjectButton;
    private Button createProjectButton;
    private Button updateProjectButton;
    private Button getProjectButton;
    private Button addAuthorButton;
    private Label projectIDLabel;
    private Label projectLabel;
    private TextArea projectNameTextArea;
    private TextArea projectDescriptionTextArea;
    private TextArea projectKeywordsTextArea;
    private ProjectService projectService;
    private Grid<AuthorDto> projectAuthorsGrid;
    private Button refreshProjectsGridButton;
    private Grid<ProjectDto> projectsGrid;
    public ProjectLayout(){

        initProjectService();
        initProjectIDLabel();
        initProjectLabel();
        initProjectDescriptionTextArea();
        initProjectDescriptionTextField();
        initProjectIDIntegerField();
        initProjectKeywordsTextArea();
        initProjectKeywordsTextField();
        initProjectNameTextArea();
        initProjectNameTextField();
        initProjectAuthorsGrid();
        initAuthorIDIntegerField();
        initCreateProjectButton();
        initDeleteProjectButton();
        initGetProjectButton();
        initUpdateProjectButton();
        initAddAuthorButton();
        initRefreshProjectsGridButton();
        initProjectsGrid();
        add(new HorizontalLayout(projectIDIntegerField,projectNameTextField, projectDescriptionTextField, projectKeywordsTextField),
                new HorizontalLayout(getProjectButton, createProjectButton, deleteProjectButton, updateProjectButton),
                authorIDIntegerField, addAuthorButton,
               new HorizontalLayout(projectLabel, projectIDLabel, projectNameTextArea, projectDescriptionTextArea, projectKeywordsTextArea),
                projectAuthorsGrid, refreshProjectsGridButton, projectsGrid);
    }

    private void initRefreshProjectsGridButton(){
        refreshProjectsGridButton = new Button("Refresh projects grid", clickEvent ->{
            try {
                projectsGrid.setItems(projectService.getProjects().get());
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        });
    }

    private void initProjectsGrid(){
        projectsGrid = new Grid<>();
        projectsGrid.addColumn(ProjectDto::getId).setHeader("ID").setSortable(true);
        projectsGrid.addColumn(ProjectDto::getNameEn).setHeader("Name");
        projectsGrid.addColumn(ProjectDto::getDescEn).setHeader("Decryption");
        projectsGrid.addColumn(ProjectDto::getKeywords).setHeader("Keywords");
    }
    private void initProjectService(){
        projectService = new ProjectService();
    }

    private void initProjectAuthorsGrid(){
        projectAuthorsGrid = new Grid<>();
        projectAuthorsGrid.addColumn(AuthorDto::getId).setHeader("Author ID").setSortable(true);
        projectAuthorsGrid.addComponentColumn(this::buildRemoveButton);
    }

    private void initProjectIDIntegerField(){
        projectIDIntegerField = new IntegerField("Project ID");
        projectIDIntegerField.setMin(0);
    }
    private void initProjectNameTextField(){
        projectNameTextField = new TextField("Project Name");
    }
    private void initProjectDescriptionTextField(){
        projectDescriptionTextField = new TextField("Project Description");
    }
    private void initProjectKeywordsTextField(){
        projectKeywordsTextField = new TextField("Project Keywords");
    }
    private void initDeleteProjectButton(){
        deleteProjectButton = new Button("Delete project", clickEvent ->{
           if(!projectIDIntegerField.isEmpty()){
               try {
                   projectService.deleteProject(projectIDIntegerField.getValue().longValue());
                   Notification.show("Deleted").setPosition(Notification.Position.TOP_CENTER);
                   projectIDLabel.setText("");
                   projectAuthorsGrid.setItems(new ArrayList<>());
                   projectDescriptionTextArea.setValue("");
                   projectKeywordsTextArea.setValue("");
                   projectNameTextArea.setValue("");
               } catch (ExecutionException | InterruptedException | UnexpectedException e) {
                   e.printStackTrace();
               }
           }
        });
    }

    private void initAuthorIDIntegerField(){
        authorIDIntegerField = new IntegerField("Author ID");
        authorIDIntegerField.setMin(0);
    }

    private void initAddAuthorButton(){
        addAuthorButton = new Button("Add Author", clickEvent ->{
           if(!authorIDIntegerField.isEmpty() && !projectIDLabel.getText().isBlank()){
               try {
                   if(projectService.addAuthor(authorIDIntegerField.getValue().longValue(), Long.decode(projectIDLabel.getText()))){
                       projectAuthorsGrid.setItems(projectService.getProjectAuthors(Long.decode(projectIDLabel.getText())).get());
                   }
                   else
                       Notification.show("Author not found").setPosition(Notification.Position.TOP_CENTER);
               } catch (ExecutionException | InterruptedException | UnexpectedException e) {
                   e.printStackTrace();
               }
           }
        });
    }

    private void initProjectIDLabel(){
        projectIDLabel = new Label("");
    }

    private void initProjectLabel(){
        projectLabel = new Label("Project ID");
    }
    private void initCreateProjectButton(){
        createProjectButton = new Button("Create Project", clickEvent -> {
           if(projectNameTextField.isEmpty() || projectNameTextField.getValue().isBlank() ||
                   projectKeywordsTextField.isEmpty() || projectKeywordsTextField.getValue().isBlank() ||
                   projectDescriptionTextField.isEmpty() || projectDescriptionTextField.getValue().isBlank()){
               Notification.show("Projects keywords, name, description fields must not be empty or blank").setPosition(Notification.Position.TOP_CENTER);
               return;
           }
           ProjectDto projectDto = new ProjectDto();
           projectDto.setDescEn(projectDescriptionTextField.getValue());
            projectDto.setKeywords(projectKeywordsTextField.getValue());
            projectDto.setNameEn(projectNameTextField.getValue());
            try {
                Optional<Long> id = projectService.createProject(projectDto);
                if(id.isPresent()){
                    Notification.show("Project Created with ID: "+id.get());
                    projectIDIntegerField.setValue(id.get().intValue());
                    getProjectButton.click();
                }
            } catch (UnexpectedException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    private void initUpdateProjectButton(){
        updateProjectButton = new Button("Update Project", clickEvent -> {
            if(projectNameTextField.isEmpty() || projectNameTextField.getValue().isBlank() ||
                    projectKeywordsTextField.isEmpty() || projectKeywordsTextField.getValue().isBlank() ||
                    projectDescriptionTextField.isEmpty() || projectDescriptionTextField.getValue().isBlank()){
                Notification.show("Projects keywords, name, description fields must not be empty or blank").setPosition(Notification.Position.TOP_CENTER);
                return;
            }
            ProjectDto projectDto = new ProjectDto();
            projectDto.setDescEn(projectDescriptionTextField.getValue());
            projectDto.setKeywords(projectKeywordsTextField.getValue());
            projectDto.setNameEn(projectNameTextField.getValue());
            projectDto.setId(Long.decode(projectIDLabel.getText()));
            try {
                Optional<ProjectDto> project = projectService.updateProject(projectDto);
                if(project.isPresent()){
                    Notification.show("Project Updated with ID: "+project.get().getId());
                    projectIDIntegerField.setValue(project.get().getId().intValue());
                    getProjectButton.click();
                }
                else
                    Notification.show("Project with ID: "+projectIDLabel.getText()+" not Found");
            } catch (UnexpectedException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    private void initGetProjectButton(){
        getProjectButton = new Button("Get Project", clickEvent -> {
           if(!projectIDIntegerField.isEmpty()){
               try {
                   Optional<ProjectDto> projectDto = projectService.getProject(projectIDIntegerField.getValue().longValue());
                   if(projectDto.isEmpty())
                       Notification.show("Project not found").setPosition(Notification.Position.TOP_CENTER);
                   else{
                       projectIDLabel.setText(projectDto.get().getId().toString());
                       projectDescriptionTextArea.setValue(projectDto.get().getDescEn());
                       projectKeywordsTextArea.setValue(projectDto.get().getKeywords());
                       projectNameTextArea.setValue(projectDto.get().getNameEn());
                       projectAuthorsGrid.setItems(projectService.getProjectAuthors(projectDto.get().getId()).get());
                   }
               } catch (UnexpectedException e) {
                   e.printStackTrace();
               }
           }
        });
    }
    private void initProjectNameTextArea(){
        projectNameTextArea = new TextArea("Project Name");
        projectNameTextArea.setReadOnly(true);
    }
    private void initProjectDescriptionTextArea(){
        projectDescriptionTextArea = new TextArea("Project Description");
        projectDescriptionTextArea.setReadOnly(true);
    }
    private void initProjectKeywordsTextArea(){
        projectKeywordsTextArea = new TextArea("Project Keywords");
        projectKeywordsTextArea.setReadOnly(true);
    }

    private Button buildRemoveButton(AuthorDto a) {
        return new Button("Remove Author", clickEvent -> {
            try {
                if(projectService.removeAuthorFromProject(Long.decode(projectIDLabel.getText()), a.getId()))
                    Notification.show("Removed").setPosition(Notification.Position.TOP_CENTER);
            } catch (ExecutionException | UnexpectedException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                projectAuthorsGrid.setItems(projectService.getProjectAuthors(Long.decode(projectIDLabel.getText())).get());
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        });
    }

}
