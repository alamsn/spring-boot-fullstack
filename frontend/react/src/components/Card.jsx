'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Button,
    useColorModeValue,
} from '@chakra-ui/react'
import CustomerDeleteButtonWithAlert from "./CustomerDeleteButtonWithAlert.jsx";
import CustomerUpdateDrawerForm from "./CustomerUpdateDrawerForm.jsx";

export default function CardWithImage({ id, name, email, age, gender, fetchCustomers }) {
    return (
        <Center py={6}>
            <Box
                minW={'250px'}
                maxW={'250px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={`https://randomuser.me/api/portraits/med/${ gender === 'MALE' ? 'men' : 'women' }/${id}.jpg`}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={0} align={'center'} mb={5}>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                    </Stack>

                    <Stack direction={'row'} justify={'center'} spacing={6}>
                        <Stack spacing={0} align={'center'}>
                            <Text fontSize={'sm'} color={'gray.500'}>
                               ID - {id}
                            </Text>
                        </Stack>
                        <Stack spacing={0} align={'center'}>
                            <Text fontSize={'sm'} color={'gray.500'}>
                                {gender}
                            </Text>
                        </Stack>
                    </Stack>

                    <Stack direction={"row"} spacing={6} justify={"center"}>
                        <CustomerUpdateDrawerForm
                            id={id}
                            name={name}
                            email={email}
                            age={age}
                            gender={gender}
                            fetchCustomers={fetchCustomers}
                        />
                        <CustomerDeleteButtonWithAlert
                            id={id}
                            fetchCustomers={fetchCustomers}
                        />
                    </Stack>
                </Box>
            </Box>
        </Center>
    )
}