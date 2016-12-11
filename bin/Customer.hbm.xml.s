<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD//EN"
    "http://hibernate.sourceforge.net/hibernate-mapping-2.0.dtd">

<hibernate-mapping>
    <class name="server.login.domain.Customer" table="CUSTOMER">
        <id name="id" column="CID">
            <generator class="increment" />
        </id>
        <property name="username" column="USERNAME" />
        <property name="password" column="PASSWORD" />
    </class>
</hibernate-mapping>