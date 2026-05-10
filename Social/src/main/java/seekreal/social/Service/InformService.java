package seekreal.social.Service;

import pojo.Social.PublicInformation;

import java.util.List;

public interface InformService {
    public void addPublic(String rootUsername, String password,String text);
    public List<PublicInformation> getPublic();
}
