package com.twk.thymeleafprac.service.attch_file;

import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.mapper.AttchFileGrpMapper;
import com.twk.thymeleafprac.mapper.AttchFileMapper;
import com.twk.thymeleafprac.mapper.MemberDirectoryMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Log
@Service
public class AttchFileServiceImpl implements AttchFileService {
    @Autowired
    AttchFileMapper attchFileMapper;

    @Autowired
    MemberDirectoryMapper memberDirectoryMapper;

    @Autowired
    AttchFileGrpMapper attchFileGrpMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String insertAttchFile(AttchFileParam param) {
        try {
            param.setFileType(param.getFileType().toUpperCase(Locale.ROOT));
            int result = attchFileMapper.insertAttchFile(param);

            if (result == 1) {
                return param.getFileId();
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return null;
    }

    @Override
    public List<AttchFileParam> selectAttchFileListByDirectoryId(String directoryId, String key) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(key);
            log.info(directoryId);
        }
        log.info("selectAttchFileListByDirectoryId : " + attchFileMapper.selectAttchFileListByDirectoryId(directoryId).toString());
        return attchFileMapper.selectAttchFileListByDirectoryId(directoryId);
    }

    @Override
    public AttchFileParam selectAttchFileByFileId(String id) {
        return attchFileMapper.selectAttchFileByFileId(id);
    }

    @Override
    public List<Map<String,Object>> selectAttchFileListByMemberKeyAndFileType(String memberKey, String fileType) {
        log.info("memberKey : " + memberKey);
        log.info("fileType: " + fileType);
        List<Map<String,Object>> list = null;

        if( fileType == null) {
            return attchFileMapper.selectAttchFileListByMemberKeyAndFileType(memberKey, fileType);
        }
        if( fileType.equals("PDF") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndPdf(memberKey, fileType);
        } else if( fileType.equals("HWP") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndHwp(memberKey, fileType);
        } else if ( fileType.equals("IMG") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndImg(memberKey, fileType);
        } else if ( fileType.equals("PPT") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndPpt(memberKey, fileType);
        } else if ( fileType.equals("EXCEL") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndExcel(memberKey, fileType);
        } else if ( fileType.equals("DOC") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndDoc(memberKey, fileType);
        } else if ( fileType.equals("MUSICVIDEO") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndMusicVideo(memberKey, fileType);
        } else if ( fileType.equals("CAD") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndCad(memberKey, fileType);
        } else if ( fileType.equals("HTML") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndHtml(memberKey, fileType);
        } else if ( fileType.equals("ZIP") ) {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndZip(memberKey, fileType);
        } else {
            list = attchFileMapper.selectAttchFileListByMemberKeyAndFileType(memberKey, fileType);
        }
        return list;
    }

    @Override
    public Long selectAttchFileSizeByMemberKeyAndId(String memberKey, String id) {
        List<AttchFileParam> list =  attchFileMapper.selectAttchFileSizeByMemberKeyAndId(memberKey, id);
        long filesTotalSize = 0L;
        if( list != null ) {
            for ( AttchFileParam param : list ) {
                filesTotalSize = (Long)filesTotalSize +  (Long) param.getFileSize();
//                log.info(filesTotalSize + "");
            }
        }
        return filesTotalSize;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateAttchFileDirectoryId( List<AttchFileParam> list) {
        try {

            for ( AttchFileParam param : list ) {
                log.info("attchFile 업데이트 : " + param.toString());
                int result = attchFileMapper.updateAttchFileDirectoryId(param);
                if (result != 1) {
                    log.info("attchFile 업데이트 실패 : " + result);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return 99;
                }
            }

            return 1;

        } catch( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 99;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AttchFileParam deleteAttchFileAndAttchFileGrp(AttchFileParam param) {

        try {
            int result1 = attchFileMapper.deleteAttchFile( param );
            int result2 = attchFileGrpMapper.deleteAttchFileGrp( param );
            log.info("result1 = " + result1 + ", result2 = " + result2);
            if ( result1 > -1 || result2 > -1 ) {
                return param;
            }

        } catch ( Exception e ) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    public List<HashMap<String, Object>> selectAttchOnlyDriveMine(String directoryId, String memberKey, String creUserId) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
            log.info(directoryId);
        }
        return attchFileMapper.selectAttchOnlyDriveMine(directoryId, memberKey, creUserId);
    }

    @Override
    public List<HashMap<String, Object>> selectAttchOnlyDriveNotMine(String directoryId, String memberKey, String creUserId) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
            log.info(directoryId);
        }
        return attchFileMapper.selectAttchOnlyDriveNotMine(directoryId, memberKey, creUserId);
    }

    @Override
    public List<HashMap<String, Object>> selectAttchTaskBoardMine(String directoryId, String memberKey, String creUserId) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
            log.info(directoryId);
        }
        return attchFileMapper.selectAttchTaskBoardMine(directoryId, memberKey, creUserId);
    }

    @Override
    public List<HashMap<String, Object>> selectAttchTaskBoardNotMineButTaskUserList(String directoryId, String memberKey, String creUserId) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
            log.info(directoryId);
        }
        return attchFileMapper.selectAttchTaskBoardNotMineButTaskUserList(directoryId, memberKey, creUserId);
    }

    @Override
    public List<HashMap<String, Object>> selectAttchTaskCmtBoardMine(String directoryId, String memberKey, String creUserId) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
            log.info(directoryId);
        }
        return attchFileMapper.selectAttchTaskCmtBoardMine(directoryId, memberKey, creUserId);
    }

    @Override
    public List<HashMap<String, Object>> selectAttchTaskCmtBoardNotMineButTaskUserList(String directoryId, String memberKey, String creUserId) {
        if ( directoryId.equals("main") ) {
            directoryId = memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
            log.info(directoryId);
        }
        return attchFileMapper.selectAttchTaskCmtBoardNotMineButTaskUserList(directoryId, memberKey, creUserId);
    }

    @Override
    public int checkAttchFileIsOtherUpload(List<AttchFileParam> attchFileList, String id) {
        AttchFileParam param = new AttchFileParam();
        for ( AttchFileParam attchFileParam : attchFileList ) {
            param = attchFileMapper.selectAttchFileByFileId( attchFileParam.getFileId() );
            if ( !param.getCreUserId().equals( id ) ) {
                return 99;
            }
        }
        return 1;
    }
}
