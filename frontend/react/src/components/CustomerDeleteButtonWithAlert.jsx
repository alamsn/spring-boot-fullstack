import {
    AlertDialog, AlertDialogBody,
    AlertDialogContent, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button, useColorModeValue,
    useDisclosure
} from "@chakra-ui/react";
import React, { useRef } from "react";
import {deleteCustomer } from "../services/client.js";
import {errorNotification, infoNotification } from "../services/notfication.js";


const onDelete = ({id, fetchCustomers }) => {
    deleteCustomer(id)
        .then(response => {
            console.log(response);
            infoNotification(
                `Customer Deleted`,
                `Customer id: ${id} was successfully deleted`);
            fetchCustomers();
        }).catch(err => {
        errorNotification(
            err.code,
            err.response.data.message);
        console.log(err);
    }).finally(() => {
    })
    return id;
}
const CustomerDeleteButtonWithAlert = ({ id, fetchCustomers }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = useRef()

    return (
        <>
        <Button
            onClick={onOpen}
            w={'40%'}
            mt={8}
            bg={useColorModeValue('#d11a2a', 'gray.900')}
            color={'white'}
            rounded={'md'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg',
            }}>
            Delete
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Customer
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure? You can't undo this action afterwards.
                        </AlertDialogBody>
                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button colorScheme='red' onClick={() => onDelete({ id, fetchCustomers })} ml={3}>
                            Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

export default CustomerDeleteButtonWithAlert;