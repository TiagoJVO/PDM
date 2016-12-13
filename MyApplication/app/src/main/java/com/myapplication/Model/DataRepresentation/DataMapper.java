package com.myapplication.Model.DataRepresentation;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.myapplication.Model.DataRepresentation.DTO.MovieInfoDTO;
import com.myapplication.Model.DataRepresentation.DTO.MovieListShortInfoDTO;
import com.myapplication.Model.DataRepresentation.DTO.MovieShortInfoDTO;

public class DataMapper {
    public MovieListShortInfo convertFrom(MovieListShortInfoDTO body) {

        List<MovieShortInfo> result = new LinkedList<>();

        for (MovieShortInfoDTO movieShortInfoDTO : body.getList()) {
            result.add(new MovieShortInfo(  movieShortInfoDTO.getTitle(),
                                            movieShortInfoDTO.isAdult(),
                                            movieShortInfoDTO.getOverview(),
                                            movieShortInfoDTO.getReleaseDate(),
                                            movieShortInfoDTO.getPoster_path(),
                                            movieShortInfoDTO.getRating(),
                                            movieShortInfoDTO.getId()));
        }

        return new MovieListShortInfo(result,body.getNumberOfPages(), body.getPage());
    }


    public MovieInfo convertFrom(MovieInfoDTO body) {

        ArrayList<MovieInfo.Company> companies = convertCompanyDTO(body.getCompanies());
        ArrayList<MovieInfo.Country> countries = convertCountryDTO(body.getCountries());
        ArrayList<MovieInfo.Gender> genders = convertGenderDTO(body.getGenres());

        return new MovieInfo(body.getTitle(),
                            body.getReleaseDate(),
                            body.getOverview(),
                            body.getTagline(),
                            body.getRating(),
                            genders,
                            body.getHomepage(),
                            companies,
                            countries,
                            body.getPoster_path(),
                            body.getId());
    }

    private ArrayList<MovieInfo.Gender> convertGenderDTO(ArrayList<MovieInfoDTO.GenreDTO> genres) {
        ArrayList<MovieInfo.Gender> result = new ArrayList<>();
        for (MovieInfoDTO.GenreDTO gender : genres) {
            result.add(new MovieInfo.Gender(gender.getId(),gender.getName()));
        }
        return result;
    }

    private ArrayList<MovieInfo.Country> convertCountryDTO(ArrayList<MovieInfoDTO.CountryDTO> countries) {
        ArrayList<MovieInfo.Country> result = new ArrayList<>();
        for (MovieInfoDTO.CountryDTO country : countries) {
            result.add(new MovieInfo.Country(country.getId(),country.getName()));
        }
        return result;
    }

    private ArrayList<MovieInfo.Company> convertCompanyDTO(ArrayList<MovieInfoDTO.CompanyDTO> companies) {
        ArrayList<MovieInfo.Company> result = new ArrayList<>();
        for (MovieInfoDTO.CompanyDTO company : companies) {
            result.add(new MovieInfo.Company(company.getId(),company.getName()));
        }
        return result;
    }


}
