package carmelo.hibernate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

public class BaseDao<T, PK extends java.io.Serializable> {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(BaseDao.class);

	private Class<T> entityClass;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseDao() {

		Type genType = getClass().getGenericSuperclass();

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		entityClass = (Class) params[0];

	}

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public PK save(T entity) {

		return (PK) getSession().save(entity);

	}

	public void saveOrUpdate(T entity) {

		getSession().saveOrUpdate(entity);

	}

	public void update(T entity) {

		getSession().update(entity);

	}

	public void merge(T entity) {

		getSession().merge(entity);

	}

	public void delete(PK id) {

		getSession().delete(this.get(id));

	}

	public void deleteObject(T entity) {

		getSession().delete(entity);

	}

	public boolean exists(PK id) {

		return get(id) != null;

	}

	@SuppressWarnings("unchecked")
	public T load(PK id) {

		return (T) getSession().load(this.entityClass, id);

	}

	@SuppressWarnings("unchecked")
	public T get(PK id) {

		return (T) getSession().get(this.entityClass, id);

	}

	public int countAll() {

		Criteria criteria = createCriteria();

		return Integer.valueOf(criteria.setProjection(Projections.rowCount())

		.uniqueResult().toString());

	}

	public int countAll(Criteria criteria) {

		return Integer.valueOf(criteria.setProjection(Projections.rowCount())

		.uniqueResult().toString());

	}

	public void deleteAll(Collection<?> entities) {

		if (entities == null)

			return;

		for (Object entity : entities) {

			getSession().delete(entity);

		}

	}

	@SuppressWarnings("unchecked")
	public List<T> list() {

		return createCriteria().list();

	}

	@SuppressWarnings("unchecked")
	public List<T> list(Criteria criteria) {

		return criteria.list();

	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> List<T> list(DetachedCriteria criteria) {

		return (List<T>) list(criteria.getExecutableCriteria(getSession()));

	}

	@SuppressWarnings("unchecked")
	public List<T> list(String orderBy, boolean isAsc) {

		Criteria criteria = createCriteria();

		if (isAsc) {

			criteria.addOrder(Order.asc(orderBy));

		} else {

			criteria.addOrder(Order.desc(orderBy));

		}

		return criteria.list();

	}

	public List<T> list(String propertyName, Object value) {

		Criterion criterion = Restrictions

		.like(propertyName, "%" + value + "%");

		return list(criterion);

	}

	@SuppressWarnings("unchecked")
	private List<T> list(Criterion criterion) {

		Criteria criteria = createCriteria();

		criteria.add(criterion);

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<T> list(Criterion... criterions) {

		return createCriteria(criterions).list();

	}

	@SuppressWarnings("unchecked")
	public T uniqueResult(String propertyName, Object value) {

		Criterion criterion = Restrictions.eq(propertyName, value);

		return (T) createCriteria(criterion).uniqueResult();

	}

	public T uniqueResult(Criterion... criterions) {

		Criteria criteria = createCriteria(criterions);

		return uniqueResult(criteria);

	}

	@SuppressWarnings("unchecked")
	public T uniqueResult(Criteria criteria) {

		return (T) criteria.uniqueResult();

	}

	public Criteria distinct(Criteria criteria) {

		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		return criteria;

	}

	public void flush() {

		getSession().flush();

	}

	public void clear() {

		getSession().clear();

	}

	public Criteria createCriteria() {

		return getSession().createCriteria(entityClass);

	}

	public Criteria createCriteria(Criterion... criterions) {

		Criteria criteria = createCriteria();

		for (Criterion c : criterions) {

			criteria.add(c);

		}

		return criteria;

	}

	public List<T> findPage(Criteria criteria, int pageNo, int pageSize) {

		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);

		return list(criteria);

	}

	public List<Object[]> queryBySql(String sql) {
		List<Object[]> list = this.getSession().createSQLQuery(sql).list();
		return list;
	}

	public int executeBySql(String sql) {
		int result;
		SQLQuery query = this.getSession().createSQLQuery(sql);
		result = query.executeUpdate();
		return result;
	}

}