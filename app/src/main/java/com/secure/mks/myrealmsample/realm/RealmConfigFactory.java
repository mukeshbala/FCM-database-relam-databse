package com.secure.mks.myrealmsample.realm;

import io.realm.RealmConfiguration;

public class RealmConfigFactory {

    public static RealmConfiguration createAdminRealmRealmConfiguration() {
        return new RealmConfiguration.Builder().name("SAMPLE_MODILE")
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
