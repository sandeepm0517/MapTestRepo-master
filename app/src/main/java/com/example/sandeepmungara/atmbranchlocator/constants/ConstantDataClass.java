package com.example.sandeepmungara.atmbranchlocator.constants;

import com.example.sandeepmungara.atmbranchlocator.viewmodel.ResultsModel;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class ConstantDataClass {

    private static List<ResultsModel> resultsModelsList;
    private static Marker resultsModels;

    public static void setData(List<ResultsModel> resultsModels) {
        resultsModelsList = resultsModels;
    }

    public static List<ResultsModel> getData() {
        return resultsModelsList;
    }

    public static void setDataForOneItem(Marker resultsModel) {
        resultsModels = resultsModel;
    }

    public static Marker getResultsModels() {
        return resultsModels;
    }
}
