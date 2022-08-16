package com.twk.thymeleafprac.service.member_directory;

import com.twk.thymeleafprac.domain.AccessPageParam;
import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.domain.util.UtilParam;
import com.twk.thymeleafprac.mapper.AttchFileMapper;
import com.twk.thymeleafprac.mapper.MemberDirectoryMapper;
import com.twk.thymeleafprac.util.Util;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Service
public class MemberDirectoryServiceImpl implements MemberDirectoryService{
    @Autowired
    MemberDirectoryMapper memberDirectoryMapper;

    @Autowired
    AttchFileMapper attchFileMapper;

    @Override
    public List<HashMap<String, Object>> selectMemberDirectoryList(AccessPageParam accessPageParam) {
        log.info("자 보자ㅏㅏ : " + accessPageParam.toString());
        List<HashMap<String, Object>> list = null;
        if (accessPageParam.getMemberPath().equals("main") ) {
            accessPageParam.setMemberPath("drive");
            list = memberDirectoryMapper.selectMemberDirectoryListByMain(accessPageParam);
        } else {
            MemberDirectoryParam memberDirectoryParam =
                    memberDirectoryMapper.selectMemberDirectoryById(accessPageParam.getMemberPath());
                    accessPageParam.setMemberKey(memberDirectoryParam.getMemberKey());
                    accessPageParam.setMemberPath(memberDirectoryParam.getVisualDirectoryLocation());
                    int count = memberDirectoryParam.getVisualDirectoryLocation().length() -
                                memberDirectoryParam.getVisualDirectoryLocation().replace("/", "").length() +1;
                    accessPageParam.setCount(count);
                    log.info("memberDirectoryParam : " + memberDirectoryParam.toString());
                    log.info("accessPageParam : "  + accessPageParam.toString());
                    log.info("path count : " + count);
            list = memberDirectoryMapper.selectMemberDirectoryListByLocation(accessPageParam);
        }

        return list;
    }

    @Override
    public MemberDirectoryParam selectMemberDirectoryById(AccessPageParam accessPageParam) {
        return memberDirectoryMapper.selectMemberDirectoryById(accessPageParam.getMemberPath());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createMemberDirectory(MemberDirectoryParam memberDirectoryParam, String id) {
        try {
            log.info("ㅁㄴㅇㄴㅁㅇㄴㅁㅇㄴㅇㄴ : " + memberDirectoryParam.toString());
            String test = memberDirectoryParam.getDirectoryId();
            if( test.substring( test.length()-1, test.length() ).equals( "?" ) ) {
                memberDirectoryParam.setDirectoryId( test.substring( 0, test.length()-1 ) );
            }

            MemberDirectoryParam parent = null;
            if(memberDirectoryParam.getDirectoryId().equals("main")) {
                parent = memberDirectoryMapper.selectMemberDirectoryMain(memberDirectoryParam.getMemberKey());
            } else {
                parent = memberDirectoryMapper.selectMemberDirectoryById(memberDirectoryParam.getDirectoryId());
            }
            String memberDirectoryId = null;
            do {
                memberDirectoryId = Util.getRandomStringUtils(100);
            } while (memberDirectoryMapper.selectMemberDirectoryById(memberDirectoryId) != null);

            memberDirectoryParam.setDirectoryId(memberDirectoryId);
            memberDirectoryParam.setVisualDirectoryLocation(parent.getVisualDirectoryLocation() + "/" + memberDirectoryParam.getDirectoryNm());
            memberDirectoryParam.setCreUserId(id);
            memberDirectoryParam.setUpdUserId(id);
            int result = memberDirectoryMapper.createMemberDirectory(memberDirectoryParam);
            if ( result == 1 ) {
                return result;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return 99;
    }

    @Override
    public List<HashMap<String, Object>> selectMemberDirectoryByMemberPathList(AccessPageParam accessPageParam) {
        MemberDirectoryParam param = new MemberDirectoryParam();
        List<HashMap<String, Object>> list = new ArrayList<>();
        log.info("accessPageParam : " +  accessPageParam.toString());

        if(accessPageParam.getMemberPath().equals("main")) {
            accessPageParam.setMemberPath("drive");
            HashMap<String, Object> map = memberDirectoryMapper.selectMemberDirectoryByMemberPath(accessPageParam);
            map.put("name", "드라이브");
            list.add(map);
        } else {
           HashMap<String, Object> parent = memberDirectoryMapper.selectMemberDirectoryByMemberPath(accessPageParam);
           param.setMemberKey((String) parent.get("MEMBER_KEY"));
           param.setVisualDirectoryLocation((String) parent.get("VISUAL_DIRECTORY_LOCATION"));

           if ( param != null ) {
               String[] path = param.getVisualDirectoryLocation().split("/");

               String location = new String();
               for ( int i = 0; i < path.length; i++ ) {
                   location += path[i];
                   accessPageParam.setMemberPath(location);
                   HashMap<String, Object> map = memberDirectoryMapper.selectMemberDirectoryByMemberPath(accessPageParam);
                   if( i == 0 ) {
                       map.put("name", "드라이브");
                   } else {
                       map.put("name", path[i]);
                   }
                   list.add(map);
                   location +="/";
               }
           }
           log.info("paths: " + list.toString());
        }


        return list;
    }

    @Override
    public String selectMemberDirectoryIdByMemberKey(String memberKey) {
        return memberDirectoryMapper.selectMemberDirectoryIdByMemberKey(memberKey);
    }

    @Override
    public List<UtilParam> allDirectoryListByDirectoryId(UtilParam utilParam) {
        String[] selectedDirectoryIdList = utilParam.getListData();
        AccessPageParam accessPageParam = new AccessPageParam();
        List<MemberDirectoryParam> dList = new ArrayList<>();

        List<UtilParam> selectedTotalDirectoryAndFileList = new ArrayList<>();

        for ( String directoryId : selectedDirectoryIdList ) {
            MemberDirectoryParam param = memberDirectoryMapper.selectMemberDirectoryById(directoryId.replace(".did-", ""));

            // 각 부모 디렉터리부터 해당 디렉터리 아이디와 파일들을 리스트에 담는다.
            UtilParam parentDirectoryAndFiles = new UtilParam();
            List<AttchFileParam> parentAttchFileParamList = attchFileMapper.selectAttchFileListByDirectoryId( directoryId.replace(".did-", "") );
            
            // 내가 직접 드라이브에 업로드한 파일 외 다른 파일이 존재하는 경우 UtilParam 의 isError 에 true 설정
            for ( AttchFileParam parentFileParam : parentAttchFileParamList ) {
                log.info("어디가?? " + parentFileParam.toString());
                if ( parentFileParam.getOnlyDrive().equals("N") ) {
                    parentDirectoryAndFiles.setError( true );
                } else {
                    parentDirectoryAndFiles.setError( false );
                }
            }

            parentDirectoryAndFiles.setDirectoryId( directoryId.replace(".did-", "") );
            parentDirectoryAndFiles.setAttchFileParamList( parentAttchFileParamList );
            selectedTotalDirectoryAndFileList.add(parentDirectoryAndFiles);

            log.info(parentAttchFileParamList.toString());

            // 각 부모들의 모든 하위 디렉터리 및 파일들을 리스트에 담는다
            accessPageParam.setMemberKey( param.getMemberKey() );
            accessPageParam.setMemberPath( param.getVisualDirectoryLocation() );
            int count = param.getVisualDirectoryLocation().length() -
                    param.getVisualDirectoryLocation().replace("/", "").length();
            accessPageParam.setCount( count );
            dList = memberDirectoryMapper.selectMemberDirectoryListByLocationToParam( accessPageParam );

            for ( MemberDirectoryParam directory : dList ) {
                UtilParam directoryAndFiles = new UtilParam();
                List<AttchFileParam> attchFileParamList = attchFileMapper.selectAttchFileListByDirectoryId( directory.getDirectoryId() );

                // 내가 직접 드라이브에 업로드한 파일 외 다른 파일이 존재하는 경우 UtilParam 의 isError 에 true 설정
                for ( AttchFileParam subFileParam : attchFileParamList ) {
                    log.info("어디? " + subFileParam.toString());
                    if ( subFileParam.getOnlyDrive().equals("N") ) {
                        directoryAndFiles.setError( true );
                    } else {
                        directoryAndFiles.setError( false );
                    }
                }

                directoryAndFiles.setDirectoryId( directory.getDirectoryId() );
                directoryAndFiles.setAttchFileParamList( attchFileParamList );
                selectedTotalDirectoryAndFileList.add(directoryAndFiles);
            }
        }

        return selectedTotalDirectoryAndFileList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteDirectory(MemberDirectoryParam memberDirectoryParam) {
        try {
            int result = memberDirectoryMapper.deleteDirectory(memberDirectoryParam);
            if ( result == 1 ) {
                return result;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 99;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return 99;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int moveDirectoryToDirectory(String[] directoryIdList, String parentDirectoryId, String id) {
        try {
            MemberDirectoryParam parentDirectory = memberDirectoryMapper.selectMemberDirectoryById( parentDirectoryId );
            MemberDirectoryParam directory = null;
            List<MemberDirectoryParam> dList = new ArrayList<>();
            AccessPageParam accessPageParam =  new AccessPageParam();
            String visualDirectoryLocation = "";
            int result = 99;
            for ( String directoryId : directoryIdList ) {
                // 옮길 부모 디렉터리가 자신인 경우 넘기기
                if ( !directoryId.equals( parentDirectoryId ) ) {
                    directory = memberDirectoryMapper.selectMemberDirectoryById( directoryId );
                    visualDirectoryLocation = parentDirectory.getVisualDirectoryLocation() + "/" + directory.getDirectoryNm();
                    result = memberDirectoryMapper.moveDirectoryToDirectory( visualDirectoryLocation, directoryId );

                    if( result != 1 ) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return 99;
                    }
                    
                    //다음은 하위 디렉터리들 경로 바꾸기
                    accessPageParam.setMemberKey( directory.getMemberKey() );
                    accessPageParam.setMemberPath( directory.getVisualDirectoryLocation() );
                    int count = directory.getVisualDirectoryLocation().length() -
                            directory.getVisualDirectoryLocation().replace("/", "").length();
                    accessPageParam.setCount( count );
                    dList = memberDirectoryMapper.selectMemberDirectoryListByLocationToParam( accessPageParam );
                    for ( MemberDirectoryParam subDList : dList ) {
                        //기존 경로에 경로가 바뀔려는 상위 폴더의 바뀌기 전 경로까지만 제거하고, 바뀐 상위 폴더의 주소값을 붙여준다
                        visualDirectoryLocation =
                                parentDirectory.getVisualDirectoryLocation() + "/" + directory.getDirectoryNm() +
                                subDList.getVisualDirectoryLocation().replace(directory.getVisualDirectoryLocation(), "");

                        result = memberDirectoryMapper.moveDirectoryToDirectory( visualDirectoryLocation, subDList.getDirectoryId() );
                        if( result != 1 ) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return 99;
                        }
                    }

                } else {
                    result = 1;
                }
            }
            return result;
        } catch ( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return 99;
    }

    @Override
    public int checkDirectoryOtherUploadFilesToMove(String[] directoryIdList, String id) {
        MemberDirectoryParam directory = null;
        List<MemberDirectoryParam> dList = new ArrayList<>();
        AccessPageParam accessPageParam =  new AccessPageParam();
        String visualDirectoryLocation = "";
        int result = 99;

        for ( String directoryId : directoryIdList ) {

            directory = memberDirectoryMapper.selectMemberDirectoryById(directoryId);
            List<AttchFileParam> attchFileParamList = attchFileMapper.selectAttchFileListByDirectoryId(directoryId);

            // 먼저 상위 디렉토리부터 파일들 중 본인이 업로드하지 않은 파일들이 있는지 체크
            for (AttchFileParam fileParam : attchFileParamList) {
                if (!fileParam.getCreUserId().equals(id)) {
                    return 99;
                }
            }

            //다음은 하위 디렉터리들 파일 체크
            accessPageParam.setMemberKey( directory.getMemberKey() );
            accessPageParam.setMemberPath( directory.getVisualDirectoryLocation() );
            int count = directory.getVisualDirectoryLocation().length() -
                    directory.getVisualDirectoryLocation().replace("/", "").length();
            accessPageParam.setCount( count );

            dList = memberDirectoryMapper.selectMemberDirectoryListByLocationToParam( accessPageParam );

            for ( MemberDirectoryParam subDList : dList ) {
                List<AttchFileParam> subAttchFileParamList = attchFileMapper.selectAttchFileListByDirectoryId( subDList.getDirectoryId() );
                for ( AttchFileParam subAttchFileParam : subAttchFileParamList ) {
                    if (!subAttchFileParam.getCreUserId().equals(id)) {
                        return 99;
                    }
                }
            }

        }
        return 1;
    }
}
