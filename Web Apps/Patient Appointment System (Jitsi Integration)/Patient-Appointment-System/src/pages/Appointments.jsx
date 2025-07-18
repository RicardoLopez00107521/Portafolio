import React, { useMemo, useState, useEffect, useRef } from "react";
import {
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getPaginationRowModel,
    getSortedRowModel,
    useReactTable,
} from "@tanstack/react-table";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { ChevronDown, Copy } from "lucide-react";
import { toast } from "sonner";

import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
    createAppointment,
    createPaymentLink,
    createPrescription,
    fetchAppointments,
    fetchPatients,
    fetchServices,
    fetchSlots,
} from "@/http/api";
import { Button } from "@/components/ui/button";
import {
    DropdownMenu,
    DropdownMenuCheckboxItem,
    DropdownMenuContent,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";

import { Input } from "@/components/ui/input";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { Tabs, TabsContent } from "@/components/ui/tabs";
import { SkeletonCard } from "@/components/skeletonCard";
import { Separator } from "@/components/ui/separator";
import { fetchDoctorJitsiInfo } from "@/http/api";

export default function Appointments() {
    const [displayAppointmentCard, setDisplayAppointmentCard] = useState(false);
    const [appointmentId, setAppointmentId] = useState("");
    const [patientName, setPatientName] = useState("");
    const [patientEmail, setPatientEmail] = useState("");
    const [patientGender, setPatientGender] = useState("");
    const [patientBloodGroup, setPatientBloodGroup] = useState("");
    const [problemName, setProlemName] = useState("");
    const [serviceName, setServiceName] = useState("");
    const [servicePrice, setServicePrice] = useState("");

    const prescriptionRef = useRef();

    const queryClient = useQueryClient();

    const [selectedPatient, setSelectedPatient] = useState(null);
    const [selectedService, setSelectedService] = useState(null);
    const [selectedSlot, setSelectedSlot] = useState(null);
    const [problem, setProblem] = useState("");
    const [date, setDate] = useState("");

    const [sorting, setSorting] = useState([]);
    const [columnFilters, setColumnFilters] = useState([]);
    const [columnVisibility, setColumnVisibility] = useState({});
    const [rowSelection, setRowSelection] = useState({});

    const [modality, setModality] = useState("");

    const [meetingLink, setMeetingLink] = useState("");
    const [showJitsi, setShowJitsi] = useState(false);
    const jitsiContainerRef = useRef(null);

    const [jitsiToken, setJitsiToken] = useState("");

    useEffect(() => {
        document.title = "Appointments";
    }, []);


useEffect(() => {
    const existingScript = document.getElementById("jitsi-iframe-api");
    if (!existingScript) {
        const script = document.createElement("script");
        script.src = "https://8x8.vc/vpaas-magic-cookie-1df3e65a7c234bc18d38aac092e83b54/external_api.js";
        script.id = "jitsi-iframe-api";
        document.body.appendChild(script);
    }
}, []);

useEffect(() => {

    if (showJitsi && window.JitsiMeetExternalAPI && meetingLink && jitsiToken) {
        const url = new URL(meetingLink);
        const domain = url.hostname; // "8x8.vc"
        const appId = url.pathname.split("/")[1]; //"vpaas-magic-cookie-xxxx"
        const roomOnly = url.pathname.split("/")[2]; // "telemedicina-nombre-numero"
        const roomName = `${appId}/${roomOnly}`;

        // console.log("meetingLink:", meetingLink);
        // console.log("Domain:", domain);
        // console.log("RoomOnly:", roomOnly);
        // console.log("Token:", jitsiToken);
        // console.log("RoomName:", roomName);

        const options = {
            roomName: roomName,
            parentNode: jitsiContainerRef.current,
            jwt: jitsiToken,
            configOverwrite: {
                startWithAudioMuted: true,
                startWithVideoMuted: true,
            },
            interfaceConfigOverwrite: {
                SHOW_JITSI_WATERMARK: false,
            },
        };

        const api = new window.JitsiMeetExternalAPI(domain, options);

        api.addEventListener('readyToClose', () => {
            setShowJitsi(false);
            setMeetingLink("");
            setJitsiToken("");
        }
        );

        return () => {
            api.dispose();
        };
    }
}, [showJitsi, meetingLink, jitsiToken]);




    const currentDate = new Date();
    const formattedDate = currentDate.toLocaleString("en-US", {
        weekday: "long",
        year: "numeric",
        month: "long",
        day: "numeric",
        hour: "numeric",
        minute: "numeric",
        hour12: true,
    });

    const { data, isLoading, isError, error } = useQuery({
        queryKey: ["appointments"],
        queryFn: fetchAppointments,
    });

    const {
        data: patientData,
        isLoading: isLoadingPatientData,
        isError: isErrorPatientData,
        error: errorPatientData,
    } = useQuery({
        queryKey: ["patients"],
        queryFn: fetchPatients,
        staleTime: 10000,
    });

    const {
        data: serviceData,
        isLoading: isLoadingServiceData,
        isError: isErrorServiceData,
    } = useQuery({
        queryKey: ["services"],
        queryFn: fetchServices,
        staleTime: 10000,
    });

    const {
        data: slotData,
        isLoading: isLoadingSlotData,
        isError: isErrorSlotData,
    } = useQuery({
        queryKey: ["slots"],
        queryFn: fetchSlots,
        staleTime: 10000,
    });

    const mutation = useMutation({
        mutationFn: (data) => createAppointment(data),
        onSuccess: () => {
            queryClient.invalidateQueries(["appointments"]);
            toast("Appointment Added Successfully!", {
                description: formattedDate,
                action: {
                    label: "Ok",
                    onClick: () => console.log("Ok"),
                },
            });
        },
        onError: () => {
            toast("Error while adding the appointment", {
                description: formattedDate,
                action: {
                    label: "Ok",
                    onClick: () => console.log("Ok"),
                },
            });
        },
    });

    const handleAppointmentSubmit = async () => {
        if (selectedPatient && selectedService && selectedSlot && problem && date && modality) {
            const data = {
                Problem: problem,
                Date: date,
                PatientId: selectedPatient,
                ServiceId: selectedService,
                SlotId: selectedSlot,
                Modality: modality,
            };
            const paymentData = {
                Amount: 500,
                Currency: "inr",
            };
            mutation.mutate(data);
            setProblem("");
            setDate("");
            setModality("");
            toast("Payment Link will open soon", {
                description: formattedDate,
                action: {
                    label: "Ok",
                    onClick: () => console.log("Ok"),
                },
            });
            const paymentUrl = await createPaymentLink(paymentData);
            if (paymentUrl) {
                window.location.href = paymentUrl;
            }
        }
    };

    const viewAppointment = (data) => {
        setDisplayAppointmentCard(true);
        setPatientName(data.patient.Name);
        setPatientEmail(data.patient.Email);
        setPatientGender(data.patient.Gender);
        setPatientBloodGroup(data.patient.BloodGroup);
        setProlemName(data.Problem);
        setServiceName(data.service.Name);
        setServicePrice(data.service.Price);
        setAppointmentId(data.Id);
        setModality(data.Modality);
        setMeetingLink(data.MeetingLink);
    };

    const handlePrescriptionSubmit = (id) => {
        const prescription = prescriptionRef.current.value;

        if (prescription) {
            const data = {
                Prescription: prescription,
            };
            prescriptionMutation.mutate({ id, data });
            setDisplayAppointmentCard(false);
        }
    };

    const prescriptionMutation = useMutation({
        mutationFn: ({ id, data }) => createPrescription(id, data),
        onSuccess: () => {
            queryClient.invalidateQueries(["appointments"]);
            toast("Prescription Added Successfully!", {
                description: formattedDate,
                action: {
                    label: "Ok",
                    onClick: () => console.log("Ok"),
                },
            });
        },
        onError: () => {
            toast("Error while adding the prescription", {
                description: formattedDate,
                action: {
                    label: "Ok",
                    onClick: () => console.log("Ok"),
                },
            });
        },
    });
    const columns = useMemo(
        () => [
            {
                accessorKey: "patient.Name",
                header: "Patient",
            },
            {
                accessorKey: "Problem",
                header: "Problem",
            },
            {
                accessorKey: "Date",
                header: "Date",
            },
            {
                accessorKey: "slot.Time",
                header: "Time",
            },
            {
                accessorKey: "Modality",
                header: "Modality",
                cell: ({ row }) => {
                    const modality = row.original.Modality;
                    return (
                        <span
                            className={`px-3 py-1.5 rounded-full text-sm font-medium ${modality === "Virtual"
                                    ? "bg-purple-100 text-purple-800 border border-purple-200"
                                    : "bg-emerald-100 text-emerald-800 border border-emerald-200"
                                }`}
                        >
                            {modality === "Virtual" ? "Virtual" : "In-Person"}
                        </span>
                    );
                },
            },
        ],
        []
    );

    const table = useReactTable({
        data: data || [],
        columns,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        onColumnVisibilityChange: setColumnVisibility,
        onRowSelectionChange: setRowSelection,
        state: {
            sorting,
            columnFilters,
            columnVisibility,
            rowSelection,
        },
    });

    if (
        isLoading ||
        isLoadingPatientData ||
        isLoadingServiceData ||
        isLoadingSlotData
    ) {
        return <SkeletonCard />;
    }

    if (isError || isErrorPatientData || isErrorServiceData || isErrorSlotData) {
        return <div>Error: {error ? error.message : errorPatientData.message}</div>;
    }

    return (
        <main className="grid flex gap-4 p-4 sm:px-6 sm:py-0 md:gap-8 lg:grid-cols-3 xl:grid-cols-3">
            {!showJitsi ? (
                <div className="grid auto-rows-max items-start gap-4 md:gap-8 lg:col-span-2">
                    <Tabs defaultValue="all">
                        <TabsContent value="all">
                            <Card x-chunk="dashboard-05-chunk-3">
                                <CardHeader className="px-7">
                                    <div className="justify-between flex">
                                        <CardTitle>Appointments</CardTitle>
                                        <AlertDialog>
                                            <AlertDialogTrigger asChild>
                                                <Button>Add new Appointment</Button>
                                            </AlertDialogTrigger>
                                            <AlertDialogContent>
                                                <AlertDialogHeader>
                                                    <AlertDialogTitle>
                                                        Enter the Details of the Appointment
                                                    </AlertDialogTitle>
                                                    <AlertDialogDescription>
                                                        <Select
                                                            onValueChange={setSelectedPatient}
                                                            autoFocus={true}
                                                        >
                                                            <SelectTrigger className="w-full rounded-lg bg-background my-6">
                                                                <SelectValue placeholder="Select a Patient" />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                <SelectGroup>
                                                                    <SelectLabel>Patients List</SelectLabel>
                                                                    {patientData.map((patient) => (
                                                                        <SelectItem
                                                                            key={patient.Id}
                                                                            value={patient.Id}
                                                                        >
                                                                            {patient.Name}
                                                                        </SelectItem>
                                                                    ))}
                                                                </SelectGroup>
                                                            </SelectContent>
                                                        </Select>
                                                        <Select onValueChange={setSelectedService}>
                                                            <SelectTrigger className="w-full rounded-lg bg-background my-6">
                                                                <SelectValue placeholder="Select a Service" />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                <SelectGroup>
                                                                    <SelectLabel>Service List</SelectLabel>
                                                                    {serviceData.map((service) => (
                                                                        <SelectItem
                                                                            key={service.Id}
                                                                            value={service.Id}
                                                                        >
                                                                            {service.Name}
                                                                        </SelectItem>
                                                                    ))}
                                                                </SelectGroup>
                                                            </SelectContent>
                                                        </Select>
                                                        <Select onValueChange={setSelectedSlot}>
                                                            <SelectTrigger className="w-full rounded-lg bg-background my-6">
                                                                <SelectValue placeholder="Select a Slot" />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                <SelectGroup>
                                                                    <SelectLabel>Slot List</SelectLabel>
                                                                    {slotData.map((slot) => (
                                                                        <SelectItem key={slot.Id} value={slot.Id}>
                                                                            {slot.Time}
                                                                        </SelectItem>
                                                                    ))}
                                                                </SelectGroup>
                                                            </SelectContent>
                                                        </Select>
                                                        <Input
                                                            value={problem}
                                                            onChange={(e) => setProblem(e.target.value)}
                                                            type="text"
                                                            placeholder="Enter the Problem:"
                                                            className="w-full rounded-lg bg-background my-6"
                                                        />
                                                        <Input
                                                            value={date}
                                                            onChange={(e) => setDate(e.target.value)}
                                                            type="date"
                                                            placeholder="Enter the Date:"
                                                            className="w-full rounded-lg bg-background my-6"
                                                        />
                                                        <Select value={modality} onValueChange={setModality}>
                                                            <SelectTrigger className="w-full rounded-lg bg-background my-6">
                                                                <SelectValue placeholder="Select modality" />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                <SelectGroup>
                                                                    <SelectLabel>Modality</SelectLabel>
                                                                    <SelectItem value="Presential">In-Person</SelectItem>
                                                                    <SelectItem value="Virtual">Virtual</SelectItem>
                                                                </SelectGroup>
                                                            </SelectContent>
                                                        </Select>
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction onClick={handleAppointmentSubmit}>
                                                        Add Appointment
                                                    </AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
                                    </div>
                                    <CardDescription>
                                        All of your pending appointments.
                                    </CardDescription>
                                </CardHeader>
                                <CardContent>
                                    <div className="w-full">
                                        <div className="flex items-center py-4">
                                            <Input
                                                placeholder="Search by problem..."
                                                onChange={(event) =>
                                                    table
                                                        .getColumn("Problem")
                                                        ?.setFilterValue(event.target.value)
                                                }
                                                className="max-w-sm"
                                                autoFocus={true}
                                                tabIndex={1}
                                            />
                                            <DropdownMenu>
                                                <DropdownMenuTrigger asChild>
                                                    <Button variant="outline" className="ml-auto">
                                                        Columns <ChevronDown className="ml-2 h-4 w-4" />
                                                    </Button>
                                                </DropdownMenuTrigger>
                                                <DropdownMenuContent align="end">
                                                    {table
                                                        .getAllColumns()
                                                        .filter((column) => column.getCanHide())
                                                        .map((column) => {
                                                            return (
                                                                <DropdownMenuCheckboxItem
                                                                    key={column.id}
                                                                    className="capitalize"
                                                                    checked={column.getIsVisible()}
                                                                    onCheckedChange={(value) =>
                                                                        column.toggleVisibility(!!value)
                                                                    }
                                                                >
                                                                    {column.id}
                                                                </DropdownMenuCheckboxItem>
                                                            );
                                                        })}
                                                </DropdownMenuContent>
                                            </DropdownMenu>
                                        </div>
                                        <div className="rounded-md border">
                                            <Table>
                                                <TableHeader>
                                                    {table.getHeaderGroups().map((headerGroup) => (
                                                        <TableRow key={headerGroup.id}>
                                                            <TableHead>Sr</TableHead>
                                                            {headerGroup.headers.map((header) => {
                                                                return (
                                                                    <TableHead key={header.id}>
                                                                        {header.isPlaceholder
                                                                            ? null
                                                                            : flexRender(
                                                                                header.column.columnDef.header,
                                                                                header.getContext()
                                                                            )}
                                                                    </TableHead>
                                                                );
                                                            })}
                                                        </TableRow>
                                                    ))}
                                                </TableHeader>
                                                <TableBody>
                                                    {table.getRowModel().rows?.length ? (
                                                        table.getRowModel().rows.map((row, idx) => (
                                                            <TableRow
                                                                key={row.id}
                                                                data-state={row.getIsSelected() && "selected"}
                                                                onClick={() => viewAppointment(row.original)}
                                                            >
                                                                <TableCell>{idx + 1}</TableCell>
                                                                {row.getVisibleCells().map((cell) => (
                                                                    <TableCell key={cell.id}>
                                                                        {flexRender(
                                                                            cell.column.columnDef.cell,
                                                                            cell.getContext()
                                                                        )}
                                                                    </TableCell>
                                                                ))}
                                                            </TableRow>
                                                        ))
                                                    ) : (
                                                        <TableRow>
                                                            <TableCell
                                                                colSpan={columns.length}
                                                                className="h-24 text-center"
                                                            >
                                                                No results.
                                                            </TableCell>
                                                        </TableRow>
                                                    )}
                                                </TableBody>
                                            </Table>
                                        </div>
                                        <div className="flex items-center justify-end space-x-2 py-4">
                                            <div className="flex-1 text-sm text-muted-foreground">
                                                {table.getFilteredSelectedRowModel().rows.length} of{" "}
                                                {table.getFilteredRowModel().rows.length} row(s) selected.
                                            </div>
                                            <div className="space-x-2">
                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    onClick={() => table.previousPage()}
                                                    disabled={!table.getCanPreviousPage()}
                                                >
                                                    Previous
                                                </Button>
                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    onClick={() => table.nextPage()}
                                                    disabled={!table.getCanNextPage()}
                                                >
                                                    Next
                                                </Button>
                                            </div>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </TabsContent>
                    </Tabs>
                </div>
            ) : (
                <div className="grid auto-rows-max items-start gap-4 md:gap-8 lg:col-span-2">
                    <Card>
                        <CardHeader className="flex flex-row items-center justify-between">
                            <div>
                                <CardTitle>Video call in progress</CardTitle>
                                <CardDescription>
                                    Appointment with {patientName}
                                </CardDescription>
                            </div>
                            <Button 
                                variant="outline" 
                                onClick={() => {
                                    setShowJitsi(false);
                                    setMeetingLink("");
                                    setJitsiToken("");
                                }}
                            >
                                End call
                            </Button>
                        </CardHeader>
                        <CardContent>
                            <div
                                id="jitsi-container"
                                ref={jitsiContainerRef}
                                style={{ height: "600px", width: "100%" }}
                                className="rounded-lg overflow-hidden"
                            />
                        </CardContent>
                    </Card>
                </div>
            )}
            <div className={displayAppointmentCard ? "" : "hidden"}>
                <Card className="overflow-hidden mt-2.5" x-chunk="dashboard-05-chunk-4">
                    <CardHeader className="flex flex-row items-start bg-muted/50">
                        <div className="grid gap-0.5">
                            <CardTitle className="group flex items-center gap-2 text-lg">
                                {patientName}
                                <Button
                                    size="icon"
                                    variant="outline"
                                    className="h-6 w-6 opacity-0 transition-opacity group-hover:opacity-100"
                                >
                                    <Copy className="h-3 w-3" />
                                    <span className="sr-only">Copy Order ID</span>
                                </Button>
                            </CardTitle>
                            <CardDescription>{patientEmail}</CardDescription>
                        </div>
                    </CardHeader>
                    <CardContent className="p-6 text-sm">
                        <div className="grid gap-3">
                            <div className="font-semibold">Patient Details</div>
                            <ul className="grid gap-3">
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Name</span>
                                    <span>{patientName}</span>
                                </li>
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Email</span>
                                    <span>{patientEmail}</span>
                                </li>
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Gender</span>
                                    <span>{patientGender}</span>
                                </li>
                            </ul>
                            <Separator className="my-2" />
                            <div className="font-semibold">Health Details</div>
                            <ul className="grid gap-3">
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Blood Group</span>
                                    <span>{patientBloodGroup}</span>
                                </li>
                            </ul>
                            <Separator className="my-2" />
                            <div className="font-semibold">Problem Details</div>
                            <ul className="grid gap-3">
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Problem</span>
                                    <span>{problemName}</span>
                                </li>
                            </ul>
                            <ul className="grid gap-3">
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Service</span>
                                    <span>{serviceName}</span>
                                </li>
                            </ul>
                            <ul className="grid gap-3">
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Price</span>
                                    <span>{servicePrice}</span>
                                </li>
                            </ul>
                            <ul className="grid gap-3">
                                <li className="flex items-center justify-between">
                                    <span className="text-muted-foreground">Modality</span>
                                    <span
                                        className={`px-3 py-1.5 rounded-full text-sm font-medium ${modality === "Virtual"
                                                ? "bg-purple-100 text-purple-800 border border-purple-200"
                                                : "bg-emerald-100 text-emerald-800 border border-emerald-200"
                                            }`}
                                    >
                                        {modality === "Virtual" ? "Virtual" : "In-Person"}
                                    </span>
                                </li>
                            </ul>
                            {modality === "Virtual" && appointmentId && patientName && (
                            <div className="mt-4 flex justify-end">
                                <Button
                                    onClick={async () => {
                                        try {
                                            console.log("Joining video call for appointment ID:", appointmentId);
                                            console.log("Patient Name:", patientName);
                                            console.log("Meeting Link:", meetingLink);
                                            console.log("Jitsi Token:", jitsiToken);

                                            const response = await fetchDoctorJitsiInfo(appointmentId);
                                            if (response.meeting_url && response.token) {
                                                setMeetingLink(response.meeting_url);
                                                setJitsiToken(response.token);
                                                setShowJitsi(true);
                                            } else {
                                                toast("Could not generate video call link", {
                                                    description: "Check backend or Jitsi credentials",
                                                });
                                            }
                                        } catch (error) {
                                            toast("Error connecting to video call", {
                                                description: "Please try again later",
                                            });
                                        }
                                    }}
                                >
                                    Join video call
                                </Button>

                            </div>
                            )}


                            <div className="justify-between flex ml-auto mt-4">
                                <AlertDialog>
                                    <AlertDialogTrigger asChild>
                                        <Button>Give Prescription</Button>
                                    </AlertDialogTrigger>
                                    <AlertDialogContent>
                                        <AlertDialogHeader>
                                            <AlertDialogTitle>
                                                Enter the Details of the Prescription
                                            </AlertDialogTitle>
                                            <AlertDialogDescription>
                                                <Input
                                                    ref={prescriptionRef}
                                                    type="textarea"
                                                    placeholder="Enter the Prescription:"
                                                    className="w-full rounded-lg bg-background my-6"
                                                    autoFocus={true}
                                                />
                                            </AlertDialogDescription>
                                        </AlertDialogHeader>
                                        <AlertDialogFooter>
                                            <AlertDialogCancel>Cancel</AlertDialogCancel>
                                            <AlertDialogAction
                                                onClick={() => handlePrescriptionSubmit(appointmentId)}
                                            >
                                                Add Prescription
                                            </AlertDialogAction>
                                        </AlertDialogFooter>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </main>
    );
}

