package com.maximsachok.author_identification_demo.Layouts;

import com.maximsachok.author_identification_demo.Dto.AuthorDto;
import com.maximsachok.author_identification_demo.Dto.ProjectDto;
import com.maximsachok.author_identification_demo.Dto.SearchResultDto;
import com.maximsachok.author_identification_demo.Services.AuthorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.apache.commons.lang3.tuple.MutablePair;

import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AuthorIdentificationLayout extends VerticalLayout {

    private TextArea projectNameTextArea;
    private TextArea projectDescriptionTextArea;
    private TextArea projectKeywordsTextArea;
    private Button getPossibleAuthorIDButton;
    private Grid<SearchResultDto> possibleAuthorsGrid;
    private AuthorService authorService;
    private Label timeToLoadLabel;
    private Button refreshClassifierButton;
    public AuthorIdentificationLayout(){
        initAuthorService();
        initProjectDescriptionTextField();
        initProjectKeywordsTextField();
        initProjectNameTextField();
        initGetPossibleAuthorIDButton();
        initPossibleAuthorsGrid();
        initTimeToLoadLabel();
        initRefreshClassifierButton();
        add(new HorizontalLayout(projectNameTextArea, projectDescriptionTextArea, projectKeywordsTextArea),
                getPossibleAuthorIDButton, timeToLoadLabel, refreshClassifierButton, possibleAuthorsGrid);
    }

    private void initRefreshClassifierButton(){
        refreshClassifierButton = new Button("Refresh classifier", clickEvent -> {
            try {
                authorService.refreshClassifier();
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
            Notification.show("Classifier is updating").setPosition(Notification.Position.TOP_CENTER);
        });
    }

    private void initTimeToLoadLabel(){
        timeToLoadLabel = new Label("");
    }

    private void initPossibleAuthorsGrid(){
        possibleAuthorsGrid = new Grid<>();
        possibleAuthorsGrid.addColumn((SearchResultDto input) -> input.getAuthorDto().getId()).setHeader("Author ID");
        possibleAuthorsGrid.addColumn(SearchResultDto::getScore).setHeader("Probability").setSortable(true);
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
            long startTime = System.currentTimeMillis();
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
               List<SearchResultDto> result =  authorService.findPossibleAuthor(projectDto);
               if(result.size()==0){
                   Notification.show("Model is loading, wait a couple of minutes").setPosition(Notification.Position.TOP_CENTER);
                   return;
               }
               else{
                   possibleAuthorsGrid.setItems(result.stream());
               }
           } catch (UnexpectedException | ExecutionException | InterruptedException e) {
               e.printStackTrace();
           }
           timeToLoadLabel.setText("Time to get the result: " + (System.currentTimeMillis()-startTime));
        });
    }

}
