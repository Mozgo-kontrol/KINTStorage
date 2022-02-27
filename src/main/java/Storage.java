import java.util.HashMap;

public class Storage
{

    private HashMap<Integer, String> _storage;

    public Storage()
    {
        this._storage = Utility.loadHashmapFromFile();
    }

    public HashMap<Integer, String> getStorage(){
        return _storage;
    }
    //Returns the value to which argument key is mapped in _storage, or null
    //if _storage contains no mapping for argument key
    public String read(int key)  {
        if (_storage.containsKey(key)){
            return _storage.get(key);
        }
        return Common.FAULT;
    }

    public Integer getSize(){
        return _storage.size();
    }

    //If _storage previously contained a mapping for argument key, the old value is replaced by argument value.
    //If _storage previously did not contain a mapping for argument key, maps argument key to argument value.
    private String update(int key, String value) {
        _storage.put(key, value);
        return Common.OK;
    }

    //If _storage previously did not contain a mapping for argument key, maps argument key to argument value.
    //If _storage previously contained a mapping for argument key, the old value is replaced by argument value.
    private String create(int key, String value) {

        _storage.put(key, value);
        return Common.OK;
    }

    //Removes the mapping for argument key from _storage, if mapping exists.
    private String delete(int key) {
        if (_storage.containsKey(key)){
            _storage.remove(key);
        }
        return Common.OK;
    }
}
