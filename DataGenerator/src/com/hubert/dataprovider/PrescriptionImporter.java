package com.hubert.dataprovider;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hubert.dal.Constant;
import com.hubert.dal.entity.BlockEntity;
import com.hubert.dal.entity.PrescriptionAliasEntity;
import com.hubert.dal.entity.PrescriptionBlockLinkEntity;
import com.hubert.dal.entity.PrescriptionEntity;
import com.hubert.dal.entity.PrescriptionUnitEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class PrescriptionImporter {

    public PrescriptionImporter(BlockEntity blockEntity, Dao<PrescriptionEntity, Integer> prescriptionDao,
            Dao<PrescriptionUnitEntity, Integer> prescriptionUnitDao,
            Dao<PrescriptionBlockLinkEntity, Integer> prescriptionBlockLinkDao,
            Dao<PrescriptionAliasEntity, Integer> prescriptionAliasDao) {
        _currentBlock = blockEntity;
        _prescriptionDao = prescriptionDao;
        _prescriptionUnitDao = prescriptionUnitDao;
        _prescriptionBlockLinkDao = prescriptionBlockLinkDao;
        _prescriptionAliasDao = prescriptionAliasDao;

    }

    public boolean doImport(String content) throws SQLException {

        if (content.indexOf("[") != 0) {
            return false;
        }

        String prescriptionName = content.substring(1);
        int nameEndIndex = prescriptionName.indexOf(']');

        String prescriptionDetail = prescriptionName.substring(nameEndIndex + 1);

        Map.Entry<String, List<String>> alias = parsePrescriptionAlias(prescriptionName.substring(0, nameEndIndex));

        // QueryBuilder<Account, String> queryBuilder = dao.queryBuilder();
        // queryBuilder.where().eq(Account.PASSWORD_FIELD_NAME, "qwerty");
        PrescriptionEntity prescription = null;
        prescriptionName = alias.getKey();
        QueryBuilder<PrescriptionEntity, Integer> queryBuilder = _prescriptionDao.queryBuilder();
        queryBuilder.where().eq(Constant.FILED_NAME, prescriptionName);
        List<PrescriptionEntity> existingValue = queryBuilder.query();
        if (existingValue != null && !existingValue.isEmpty()) {
            prescription = existingValue.get(0);
        }
        if (prescription == null) {
            prescription = new PrescriptionEntity();
            prescription.name = alias.getKey();
            _prescriptionDao.createOrUpdate(prescription);

            List<PrescriptionUnitEntity> prescriptionUnits = createPrescriptionEntity(prescriptionDetail);
            for (PrescriptionUnitEntity unit : prescriptionUnits) {
                unit.prescription = prescription;
                _prescriptionUnitDao.createOrUpdate(unit);
            }
        }

        for (String item : alias.getValue()) {
            createOrUpdatePrescriptionAlias(prescription, item);
        }

        PrescriptionBlockLinkEntity prescriptionBlockLink = new PrescriptionBlockLinkEntity();
        prescriptionBlockLink.block = _currentBlock;
        prescriptionBlockLink.prescription = prescription;
        _prescriptionBlockLinkDao.createOrUpdate(prescriptionBlockLink);

        return true;

    }

    private void createOrUpdatePrescriptionAlias(PrescriptionEntity prescription, String alias) throws SQLException {

        if (prescription.id > 0) {
            QueryBuilder<PrescriptionAliasEntity, Integer> queryBuilder = _prescriptionAliasDao.queryBuilder();
            queryBuilder.where().eq(Constant.FILED_NAME, prescription.name).and()
                    .eq(Constant.FIELD_PRESCRIPTION_ID, prescription.id).and().eq(Constant.FILED_ALIAS, alias);
            List<PrescriptionAliasEntity> existingValue = queryBuilder.query();
            if (!existingValue.isEmpty()) {
                return;
            }
        }

        PrescriptionAliasEntity aliasEntity = new PrescriptionAliasEntity();
        aliasEntity.alias = alias;
        aliasEntity.name = prescription.name;
        aliasEntity.prescription = prescription;
        _prescriptionAliasDao.createOrUpdate(aliasEntity);
    }

    private Map.Entry<String, List<String>> parsePrescriptionAlias(String content) {
        int aliasStartIndex = content.indexOf('(');
        if (aliasStartIndex < 0) {
            return new AbstractMap.SimpleEntry<String, List<String>>(content, new ArrayList<String>());
        }

        String name = content.substring(0, aliasStartIndex);
        String[] temp = content.substring(aliasStartIndex + 1, content.length() - 1).split(" ");
        return new AbstractMap.SimpleEntry<String, List<String>>(name, Arrays.asList(temp));
    }

    private List<PrescriptionUnitEntity> createPrescriptionEntity(String detail) {
        ArrayList<PrescriptionUnitEntity> result = new ArrayList<PrescriptionUnitEntity>();

        return result;
    }

    private BlockEntity _currentBlock;
    private Dao<PrescriptionEntity, Integer> _prescriptionDao;
    private Dao<PrescriptionUnitEntity, Integer> _prescriptionUnitDao;
    private Dao<PrescriptionBlockLinkEntity, Integer> _prescriptionBlockLinkDao;
    private Dao<PrescriptionAliasEntity, Integer> _prescriptionAliasDao;

}
