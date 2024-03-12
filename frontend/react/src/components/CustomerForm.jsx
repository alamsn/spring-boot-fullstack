import React from 'react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {saveCustomer, updateCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notfication.js";

const MyTextInput = ({ label, ...props }) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid, and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"warning"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </>
    );
};

const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"warning"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

// And now we can use these
const CustomerForm = ({ id, name, email, age, gender, fetchCustomers }) => {
    return (
        <>
            <Formik
                initialValues={{
                    name: name,
                    email: email,
                    age: age,
                    gender: gender,
                }}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(100, 'Must be 100 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    age: Yup.number()
                        .min(17, 'Must be at least 17 yo')
                        .max(99, 'Must be less than 99 yo')
                        .required('Required'),
                    gender: Yup.string()
                        .oneOf(
                            ['MALE', 'FEMALE'],
                            'Invalid Gender'
                        )
                        .required('Required'),
                })}
                onSubmit={(customer, { setSubmitting }) => {
                    setSubmitting(true);
                    if (id === null || id === undefined) {
                        saveCustomer(customer)
                            .then(response => {
                                console.log(response);
                                successNotification(
                                    'Customer Saved',
                                    `${customer.name} was successfully saved`);
                                fetchCustomers();
                            }).catch(err => {
                            errorNotification(
                                err.code,
                                err.response.data.message);
                                console.log(err);
                            }).finally(() => {
                                setSubmitting(false)
                            })
                    } else {
                        const customerUpdateRequest = {
                            name: customer.name,
                            email: customer.email,
                            age: customer.age,
                            gender: customer.gender
                        }
                        console.log(customerUpdateRequest);
                        updateCustomer(id, customerUpdateRequest)
                            .then(response => {
                                console.log(response);
                                successNotification(
                                    'Customer Updated',
                                    `${name} was successfully updated`);
                                fetchCustomers();
                            }).catch(err => {
                            errorNotification(
                                err.code,
                                err.response.data.message);
                            console.log(err);
                        }).finally(() => {
                            setSubmitting(false)
                        })
                    }

                }}
            >
                {({isValid, isSubmitting}) => (
                    <Form>
                        <Stack spacing={15}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="input your name"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="input your email"
                            />

                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                                placeholder="how old are you?"
                            />

                            <MySelect label="Gender" name="gender" value={gender}>
                                <option value="">Select a gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </MySelect>

                            <Button isDisabled={!isValid || isSubmitting} type="submit" colorScheme={'teal'} mt={25}>Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default CustomerForm