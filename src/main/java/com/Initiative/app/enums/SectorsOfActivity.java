package com.Initiative.app.enums;


public enum SectorsOfActivity {
    ADMINISTRATIVE_AND_SUPPORT_SERVICES("Activités de services administratifs et de soutien"),
    SPECIALIZED_SCIENTIFIC_AND_TECHNICAL_ACTIVITIES("Activités spécialisées, scientifiques et techniques"),
    AGRICULTURE_FORESTRY_FISHING("Agriculture, sylviculture, pêche"),
    ARTS_ENTERTAINMENT_AND_RECREATION("Arts, spectacles et activités récréatives"),
    TRADE_AND_REPAIR("Commerce et réparation"),
    CONSTRUCTION("Construction-BTP"),
    EDUCATION("Enseignement"),
    HOTELS_CAFES_AND_RESTAURANTS("Hôtels, cafés et restaurants"),
    INDUSTRY("Industrie"),
    INFORMATION_AND_COMMUNICATION("Information et communication"),
    WATER_SUPPLY_SANITATION_WASTE_MANAGEMENT("Production et distribution d’eau, assainissement, gestion des déchets et dépollution"),
    ELECTRICITY_GAS_VAPOR_AND_AIR_CONDITIONING("Production et distribution d’électricité, de gaz, de vapeur d’air conditionné"),
    HUMAN_HEALTH_AND_SOCIAL_ACTION("Santé humaine et action sociale"),
    BUSINESS_SERVICES("Services aux entreprises"),
    PERSONAL_SERVICES("Services aux particuliers"),
    TRANSPORTATION("Transports");

    private final String description;

    SectorsOfActivity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}