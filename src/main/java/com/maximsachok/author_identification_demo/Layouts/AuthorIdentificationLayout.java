package com.maximsachok.author_identification_demo.Layouts;

import com.maximsachok.author_identification_demo.Dto.ProjectDto;
import com.maximsachok.author_identification_demo.Services.AuthorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.rmi.UnexpectedException;
import java.util.concurrent.ExecutionException;

public class AuthorIdentificationLayout extends VerticalLayout {

    private TextArea projectNameTextArea;
    private TextArea projectDescriptionTextArea;
    private TextArea projectKeywordsTextArea;
    private Button getPossibleAuthorIDButton;
    private Label possibleAuthorIDLabel;
    private Label possibleAuthorLabel;
    private AuthorService authorService;
    public AuthorIdentificationLayout(){
        initAuthorService();
        initProjectDescriptionTextField();
        initProjectKeywordsTextField();
        initProjectNameTextField();
        initPossibleAuthorIDLabel();
        initPossibleAuthorLabel();
        initGetPossibleAuthorIDButton();
        add(new HorizontalLayout(projectNameTextArea, projectDescriptionTextArea, projectKeywordsTextArea),
                getPossibleAuthorIDButton, new HorizontalLayout(possibleAuthorLabel, possibleAuthorIDLabel));
    }

    private void initProjectDescriptionTextField(){
        projectDescriptionTextArea = new TextArea("Project Description");
    }

    private void initAuthorService(){
        authorService = new AuthorService();
    }

    private void initProjectNameTextField(){
        projectNameTextArea = new TextArea("Project Name");
    }

    private void initProjectKeywordsTextField(){
        projectKeywordsTextArea = new TextArea("Project Keywords");
    }

    private void initGetPossibleAuthorIDButton(){
        getPossibleAuthorIDButton = new Button("Get Possible Author", clickEvent ->{
           if(projectDescriptionTextArea.isEmpty() || projectDescriptionTextArea.getValue().isBlank() ||
           projectKeywordsTextArea.isEmpty() || projectKeywordsTextArea.getValue().isBlank() ||
           projectNameTextArea.isEmpty() || projectNameTextArea.getValue().isBlank()){
               Notification.show("Projects keywords, name, description fields must not be empty or blank").setPosition(Notification.Position.TOP_CENTER);
               return;
           }
           ProjectDto projectDto = new ProjectDto();
           projectDto.setNameEn(projectNameTextArea.getValue());
           projectDto.setKeywords(projectKeywordsTextArea.getValue());
           projectDto.setDescEn(projectDescriptionTextArea.getValue());
           try {
               possibleAuthorIDLabel.setText(authorService.findPossibleAuthor(projectDto).getId().toString());
           } catch (UnexpectedException | ExecutionException | InterruptedException e) {
               e.printStackTrace();
           }
        });
    }

    private void initPossibleAuthorIDLabel(){
        possibleAuthorIDLabel = new Label("");
    }
    private void initPossibleAuthorLabel(){
        possibleAuthorLabel = new Label("Possible Author ID: ");
    }
}
