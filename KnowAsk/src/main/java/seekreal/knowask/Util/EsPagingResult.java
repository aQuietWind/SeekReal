package seekreal.knowask.Util;

import co.elastic.clients.elasticsearch._types.FieldValue;import java.util.List;

public class EsPagingResult<E> {
    private List<E> list;
    private long esTotal;
    private Object[] sort;

public EsPagingResult() {
}public EsPagingResult(List<E> list, long esTotal, Object[] sort) {
this.list = list;this.esTotal = esTotal;this.sort = sort;} /**
 * 获取
 * @return list
 */ public List<E> getList(){
 return list;} /**
 * 设置
 * @param list
 */ public void setList(List<E> list){
this.list = list;} /**
 * 获取
 * @return esTotal
 */ public long getEsTotal(){
 return esTotal;} /**
 * 设置
 * @param esTotal
 */ public void setEsTotal(long esTotal){
this.esTotal = esTotal;} /**
 * 获取
 * @return sort
 */ public Object[] getSort(){
 return sort;} /**
 * 设置
 * @param sort
 */ public void setSort(Object[] sort){
this.sort = sort;}public String toString() {
 return "EsPagingResult{list = " + list + ", esTotal = " + esTotal + ", sort = " + sort + "}";
}}
